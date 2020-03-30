package build.dream.devops.aspects;

import build.dream.devops.jdbc.RoutingDataSource;
import build.dream.devops.models.log.ClearLogsModel;
import build.dream.devops.models.log.ListLogsModel;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class LogAspect {
    @Around(value = "execution(public * build.dream.devops.services.LogService.listLogs(build.dream.devops.models.log.ListLogsModel))")
    public Object listLogsAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ListLogsModel listLogsModel = (ListLogsModel) proceedingJoinPoint.getArgs()[0];
        return doAround(proceedingJoinPoint, listLogsModel.getDeploymentEnvironment(), listLogsModel.getPartitionCode(), listLogsModel.getServiceName());
    }

    @Around(value = "execution(public * build.dream.devops.services.LogService.clearLogs(build.dream.devops.models.log.ClearLogsModel))")
    public Object clearLogsAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ClearLogsModel clearLogsModel = (ClearLogsModel) proceedingJoinPoint.getArgs()[0];
        return doAround(proceedingJoinPoint, clearLogsModel.getDeploymentEnvironment(), clearLogsModel.getPartitionCode(), clearLogsModel.getServiceName());
    }

    private Object doAround(ProceedingJoinPoint proceedingJoinPoint, String deploymentEnvironment, String partitionCode, String serviceName) throws Throwable {
        String dataSourceName = StringUtils.isBlank(partitionCode) ? deploymentEnvironment + "-" + serviceName + "-log" : deploymentEnvironment + "-" + partitionCode + "-" + serviceName + "-log";
        RoutingDataSource.setDataSource(dataSourceName);
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            RoutingDataSource.clearDataSource();
        }
    }
}
