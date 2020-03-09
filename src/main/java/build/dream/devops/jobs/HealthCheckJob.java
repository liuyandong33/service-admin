package build.dream.devops.jobs;

import build.dream.common.utils.LogUtils;
import build.dream.common.utils.OkHttpUtils;
import build.dream.devops.constants.Constants;
import build.dream.devops.services.ServiceService;
import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.Session;
import okhttp3.Response;
import org.apache.commons.collections.MapUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@DisallowConcurrentExecution
public class HealthCheckJob implements Job {
    @Autowired
    private ServiceService serviceService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Map<String, Object>> serviceNodes = serviceService.listServiceNodes(null);
        for (Map<String, Object> serviceNode : serviceNodes) {
            Long serviceNodeId = MapUtils.getLongValue(serviceNode, "serviceNodeId");
            Integer port = MapUtils.getIntValue(serviceNode, "port");
            Integer status = MapUtils.getIntValue(serviceNode, "status");
            String pid = MapUtils.getString(serviceNode, "pid");
            String healthCheckPath = MapUtils.getString(serviceNode, "healthCheckPath");
            String ipAddress = MapUtils.getString(serviceNode, "ipAddress");
            String userName = MapUtils.getString(serviceNode, "userName");
            String password = MapUtils.getString(serviceNode, "password");
            int sshPort = MapUtils.getIntValue(serviceNode, "sshPort");
            Response response = null;
            try {
                response = OkHttpUtils.doGetNative("http://" + ipAddress + ":" + port + healthCheckPath);
                if (response.isSuccessful()) {
                    if (status != Constants.SERVICE_NODE_STATUS_RUNNING) {
                        serviceService.updateServiceNodeStatusAndPid(Constants.SERVICE_NODE_STATUS_RUNNING, pid, serviceNodeId);
                    }
                    continue;
                }

                handleFailure(ipAddress, userName, password, sshPort, pid, serviceNodeId);
            } catch (Exception e) {
                handleFailure(ipAddress, userName, password, sshPort, pid, serviceNodeId);
                LogUtils.error("执行健康检查失败");
            } finally {
                OkHttpUtils.closeResponse(response);
            }
        }
    }

    private void handleFailure(String ipAddress, String userName, String password, int sshPort, String pid, Long serviceNodeId) {
        Session session = null;
        try {
            session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
            if (JSchUtils.processExists(session, pid)) {
                serviceService.updateServiceNodeStatusAndPid(Constants.SERVICE_NODE_STATUS_WRONG, pid, serviceNodeId);
            } else {
                serviceService.updateServiceNodeStatusAndPid(Constants.SERVICE_NODE_STATUS_STOPPED, pid, serviceNodeId);
            }
        } catch (Exception e) {
            JSchUtils.disconnectSession(session);
        }
    }
}
