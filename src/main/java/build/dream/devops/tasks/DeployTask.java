package build.dream.devops.tasks;

import build.dream.common.domains.admin.$Service;
import build.dream.common.domains.admin.JavaOption;
import build.dream.common.domains.admin.ServiceConfiguration;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.devops.constants.ConfigurationKeys;
import build.dream.devops.constants.Constants;
import build.dream.devops.services.ServiceService;
import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
    private List<ServiceConfiguration> serviceConfigurations;
    private List<Map<String, Object>> serviceNodes;

    public DeployTask(ServiceService serviceService, $Service service, JavaOption javaOperation, List<ServiceConfiguration> serviceConfigurations, List<Map<String, Object>> serviceNodes) {
        this.serviceService = serviceService;
        this.service = service;
        this.javaOption = javaOperation;
        this.serviceConfigurations = serviceConfigurations;
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
        if (CollectionUtils.isNotEmpty(serviceConfigurations)) {
            for (ServiceConfiguration serviceConfiguration : serviceConfigurations) {
                commandStringBuilder.append(" --");
                commandStringBuilder.append(serviceConfiguration.getConfigurationKey());
                commandStringBuilder.append("=");
                commandStringBuilder.append(serviceConfiguration.getConfigurationValue());
            }
        }
        commandStringBuilder.append(SERVICE_DEPLOYED_PATH).append("/");
        commandStringBuilder.append(" > ");
        commandStringBuilder.append(service.getProgramName());
        commandStringBuilder.append(".log");
        commandStringBuilder.append(" 2>&1 & echo $!");

        String command = commandStringBuilder.toString();

        Session session = null;
        ChannelSftp channelSftp = null;
        List<Session> nodeSessions = null;
        List<ChannelSftp> nodeChannelSftps = null;
        try {
            session = JSchUtils.createSession(REPOSITORY_HOST_USERNAME, REPOSITORY_HOST_PASSWORD, REPOSITORY_HOST_IP_ADDRESS, REPOSITORY_HOST_SSH_PORT);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            InputStream inputStream = channelSftp.get(REPOSITORY_PATH + service.getProgramName() + "/" + service.getProgramName() + "-" + service.getProgramVersion() + ".jar");

            nodeSessions = new ArrayList<Session>();
            nodeChannelSftps = new ArrayList<ChannelSftp>();
            for (Map<String, Object> serviceNode : serviceNodes) {
                int status = MapUtils.getIntValue(serviceNode, "status");
                String userName = MapUtils.getString(serviceNode, "userName");
                String password = MapUtils.getString(serviceNode, "password");
                String ipAddress = MapUtils.getString(serviceNode, "ipAddress");
                int sshPort = MapUtils.getIntValue(serviceNode, "sshPort");
                Session nodeSession = JSchUtils.createSession(userName, password, ipAddress, sshPort);

                if (status == 1) {
                    String pid = MapUtils.getString(serviceNode, "pid");
                    if (JSchUtils.processExists(nodeSession, pid)) {
                        JSchUtils.killProcess(nodeSession, pid);
                    }
                }


                ChannelSftp nodeChannelSftp = (ChannelSftp) JSchUtils.openChannel(nodeSession, Constants.CHANNEL_TYPE_SFTP);
                nodeChannelSftp.connect();
                nodeSessions.add(nodeSession);
                nodeChannelSftps.add(nodeChannelSftp);
            }

            for (Session nodeSession : nodeSessions) {
                if (!JSchUtils.exists(nodeSession, SERVICE_DEPLOYED_PATH)) {
                    JSchUtils.mkdirs(nodeSession, SERVICE_DEPLOYED_PATH);
                }

                if (JSchUtils.exists(nodeSession, SERVICE_DEPLOYED_PATH + service.getProgramName() + ".jar")) {
                    JSchUtils.delete(nodeSession, SERVICE_DEPLOYED_PATH + service.getProgramName() + ".jar");
                }
            }

            List<OutputStream> outputStreams = new ArrayList<OutputStream>();
            for (ChannelSftp nodeChannelSftp : nodeChannelSftps) {
                OutputStream outputStream = nodeChannelSftp.put(SERVICE_DEPLOYED_PATH + service.getProgramName() + ".jar");
                outputStreams.add(outputStream);
            }

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
                for (OutputStream outputStream : outputStreams) {
                    outputStream.write(buffer, 0, length);
                }
            }

            for (Session nodeSession : nodeSessions) {
                String pid = JSchUtils.executeCommand(nodeSession, command);
                serviceService.updateServiceNodeStatusAndPid(1, pid, 100L);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (CollectionUtils.isNotEmpty(nodeChannelSftps)) {
                nodeChannelSftps.forEach(channelSftp1 -> JSchUtils.disconnectChannel(channelSftp1));
            }
            if (CollectionUtils.isNotEmpty(nodeSessions)) {
                nodeSessions.forEach(session1 -> JSchUtils.disconnectSession(session1));
            }
            JSchUtils.disconnectChannel(channelSftp);
            JSchUtils.disconnectSession(session);
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
