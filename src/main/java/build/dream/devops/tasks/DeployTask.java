package build.dream.devops.tasks;

import build.dream.common.domains.devops.$Service;
import build.dream.common.domains.devops.JavaOption;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.devops.constants.ConfigurationKeys;
import build.dream.devops.constants.Constants;
import build.dream.devops.services.ServiceService;
import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeployTask implements Runnable {
    private static final String REPOSITORY_HOST_IP_ADDRESS = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_IP_ADDRESS);
    private static final String REPOSITORY_HOST_USERNAME = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_USERNAME);
    private static final String REPOSITORY_HOST_PASSWORD = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_PASSWORD);
    private static final int REPOSITORY_HOST_SSH_PORT = Integer.parseInt(ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_SSH_PORT));
    private static final String REPOSITORY_PATH = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_PATH);
    private static final String SERVICE_DEPLOYED_PATH = ConfigurationUtils.getConfiguration(ConfigurationKeys.SERVICE_DEPLOYED_PATH);
    private ServiceService serviceService;
    private $Service service;
    private JavaOption javaOption;
    private List<Map<String, Object>> serviceNodes;

    public DeployTask(ServiceService serviceService, $Service service, JavaOption javaOperation, List<Map<String, Object>> serviceNodes) {
        this.serviceService = serviceService;
        this.service = service;
        this.javaOption = javaOperation;
        this.serviceNodes = serviceNodes;
    }

    @Override
    public void run() {
        StringBuilder commandStringBuilder = new StringBuilder("nohup java");
        if (Objects.nonNull(javaOption)) {
            commandStringBuilder.append(" ");
            commandStringBuilder.append(javaOption.buildJavaOpts());
        }
        commandStringBuilder.append(" -jar ").append(SERVICE_DEPLOYED_PATH).append("/").append(service.getProgramName()).append(".jar");
        commandStringBuilder.append(" --deployment.environment=").append(service.getDeploymentEnvironment());
        if (service.isPartitioned()) {
            commandStringBuilder.append(" --partition.code=").append(service.getDeploymentEnvironment());
        }
        commandStringBuilder.append(" --service.name=").append(service.getServiceName());
        commandStringBuilder.append(" --spring.cloud.zookeeper.connect-string=").append(service.getZookeeperConnectString());
        commandStringBuilder.append(" > ");
        commandStringBuilder.append(SERVICE_DEPLOYED_PATH).append("/");
        commandStringBuilder.append(service.getProgramName());
        commandStringBuilder.append(".log");
        commandStringBuilder.append(" 2>&1 & echo -e $!'\\c'");

        String command = commandStringBuilder.toString();

        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = JSchUtils.createSession(REPOSITORY_HOST_USERNAME, REPOSITORY_HOST_PASSWORD, REPOSITORY_HOST_IP_ADDRESS, REPOSITORY_HOST_SSH_PORT);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            for (Map<String, Object> serviceNode : serviceNodes) {
                deploy(serviceNode, channelSftp, command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JSchUtils.disconnectChannel(channelSftp);
            JSchUtils.disconnectSession(session);
        }
    }

    public void start() {
        new Thread(this).start();
    }

    private void deploy(Map<String, Object> serviceNode, ChannelSftp channelSftp, String command) {
        Session nodeSession = null;
        ChannelSftp nodeChannelSftp = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            long serviceNodeId = MapUtils.getLongValue(serviceNode, "serviceNodeId");
            int status = MapUtils.getIntValue(serviceNode, "status");
            String userName = MapUtils.getString(serviceNode, "userName");
            String password = MapUtils.getString(serviceNode, "password");
            String ipAddress = MapUtils.getString(serviceNode, "ipAddress");
            int sshPort = MapUtils.getIntValue(serviceNode, "sshPort");
            nodeSession = JSchUtils.createSession(userName, password, ipAddress, sshPort);

            if (status == 1) {
                String pid = MapUtils.getString(serviceNode, "pid");
                if (JSchUtils.processExists(nodeSession, pid)) {
                    JSchUtils.killProcess(nodeSession, pid);
                }
            }

            if (!JSchUtils.exists(nodeSession, SERVICE_DEPLOYED_PATH)) {
                JSchUtils.mkdirs(nodeSession, SERVICE_DEPLOYED_PATH);
            }

            if (JSchUtils.exists(nodeSession, SERVICE_DEPLOYED_PATH + "/" + service.getProgramName() + ".jar")) {
                JSchUtils.delete(nodeSession, SERVICE_DEPLOYED_PATH + "/" + service.getProgramName() + ".jar");
            }

            if (JSchUtils.exists(nodeSession, SERVICE_DEPLOYED_PATH + "/" + service.getProgramName() + ".log")) {
                JSchUtils.delete(nodeSession, SERVICE_DEPLOYED_PATH + "/" + service.getProgramName() + ".log");
            }

            inputStream = channelSftp.get(REPOSITORY_PATH + "/" + service.getProgramName() + "/" + service.getProgramName() + "-" + service.getProgramVersion() + ".jar");
            nodeChannelSftp = (ChannelSftp) JSchUtils.openChannel(nodeSession, Constants.CHANNEL_TYPE_SFTP);
            nodeChannelSftp.connect();

            outputStream = nodeChannelSftp.put(SERVICE_DEPLOYED_PATH + "/" + service.getProgramName() + ".jar");
            IOUtils.copy(inputStream, outputStream, 1024);

            String pid = JSchUtils.executeCommand(nodeSession, command);
            serviceService.updateServiceNodePid(pid, serviceNodeId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(outputStream);
            close(inputStream);
            JSchUtils.disconnectChannel(nodeChannelSftp);
            JSchUtils.disconnectSession(nodeSession);
        }
    }

    private void close(Closeable closeable) {
        if (Objects.nonNull(closeable)) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
