package build.dream.devops.jobs;

import build.dream.common.utils.LogUtils;
import build.dream.common.utils.OkHttpUtils;
import build.dream.devops.services.ServiceService;
import okhttp3.Response;
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
            Response response = null;
            try {
                response = OkHttpUtils.doGetNative("http://localhost:8989/ping/ok");
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
