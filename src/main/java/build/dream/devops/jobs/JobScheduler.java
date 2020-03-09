package build.dream.devops.jobs;

import build.dream.common.models.job.ScheduleCronJobModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.JobUtils;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {
    public void scheduler() {
        ApplicationHandler.callMethodSuppressThrow(() -> startJavaWebServiceHealthCheckJob());
        ApplicationHandler.callMethodSuppressThrow(() -> startHostHealthCheckJob());
    }

    public void startJavaWebServiceHealthCheckJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("Java_Web_Service_Health_Check_Job", "Java_Web_Service_Health_Check_Job");
        TriggerKey triggerKey = TriggerKey.triggerKey("Java_Web_Service_Health_Check_Trigger", "Java_Web_Service_Health_Check_Trigger");
        if (JobUtils.checkExists(jobKey) || JobUtils.checkExists(triggerKey)) {
            JobUtils.pauseTrigger(triggerKey);
            JobUtils.unscheduleJob(triggerKey);
            JobUtils.deleteJob(jobKey);
        }

        ScheduleCronJobModel scheduleCronJobModel = ScheduleCronJobModel.builder()
                .jobName("Java_Web_Service_Health_Check_Job")
                .jobGroup("Java_Web_Service_Health_Check_Job")
                .jobClass(JavaWebServiceHealthCheckJob.class)
                .triggerName("Java_Web_Service_Health_Check_Trigger")
                .triggerGroup("Java_Web_Service_Health_Check_Trigger")
                .cronExpression("0 */1 * * * ?")
                .build();
        JobUtils.scheduleCronJob(scheduleCronJobModel);
    }

    public void startHostHealthCheckJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("Host_Health_Check_Job", "Host_Health_Check_Job");
        TriggerKey triggerKey = TriggerKey.triggerKey("Host_Health_Check_Trigger", "Host_Health_Check_Trigger");
        if (JobUtils.checkExists(jobKey) || JobUtils.checkExists(triggerKey)) {
            JobUtils.pauseTrigger(triggerKey);
            JobUtils.unscheduleJob(triggerKey);
            JobUtils.deleteJob(jobKey);
        }

        ScheduleCronJobModel scheduleCronJobModel = ScheduleCronJobModel.builder()
                .jobName("Host_Health_Check_Job")
                .jobGroup("Host_Health_Check_Job")
                .jobClass(HostHealthCheckJob.class)
                .triggerName("Host_Health_Check_Trigger")
                .triggerGroup("Host_Health_Check_Trigger")
                .cronExpression("0 */1 * * * ?")
                .build();
        JobUtils.scheduleCronJob(scheduleCronJobModel);
    }
}
