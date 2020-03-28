package build.dream.devops.aspects;

import build.dream.devops.jdbc.RoutingDataSource;
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
    @Around(value = "execution(public * build.dream.devops.services.LogService.listLogs(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ListLogsModel listLogsModel = (ListLogsModel) proceedingJoinPoint.getArgs()[0];
        String deploymentEnvironment = listLogsModel.getDeploymentEnvironment();
        String partitionCode = listLogsModel.getPartitionCode();
        String serviceName = listLogsModel.getServiceName();
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
