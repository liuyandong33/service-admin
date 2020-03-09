package build.dream.devops.jobs;

import build.dream.common.utils.LogUtils;
import build.dream.common.utils.OkHttpUtils;
import build.dream.devops.services.ServiceService;
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
        List<Map<String, Object>> serviceNodes = serviceService.findAllServiceNodes();
        for (Map<String, Object> serviceNode : serviceNodes) {
            Long serviceNodeId = MapUtils.getLongValue(serviceNode, "serviceNodeId");
            Integer port = MapUtils.getIntValue(serviceNode, "port");
            Integer status = MapUtils.getIntValue(serviceNode, "status");
            Integer pid = MapUtils.getIntValue(serviceNode, "pid");
            String healthCheckPath = MapUtils.getString(serviceNode, "healthCheckPath");
            String ipAddress = MapUtils.getString(serviceNode, "ipAddress");
            Response response = null;
            try {
                response = OkHttpUtils.doGetNative("http://" + ipAddress + ":" + port + healthCheckPath);
                if (response.isSuccessful()) {
                    continue;
                }
            } catch (Exception e) {
                LogUtils.error("执行健康检查失败");
            } finally {
                OkHttpUtils.closeResponse(response);
            }
        }
    }
}
