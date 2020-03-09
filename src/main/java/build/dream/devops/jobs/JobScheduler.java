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
        ApplicationHandler.callMethodSuppressThrow(() -> startHealthCheckJob());
    }

    public void startHealthCheckJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("Health_Check_Job", "Health_Check_Job");
        TriggerKey triggerKey = TriggerKey.triggerKey("Health_Check_Job", "Health_Check_Job");
        if (JobUtils.checkExists(jobKey) || JobUtils.checkExists(triggerKey)) {
            JobUtils.pauseTrigger(triggerKey);
            JobUtils.unscheduleJob(triggerKey);
            JobUtils.deleteJob(jobKey);
        }

        ScheduleCronJobModel scheduleCronJobModel = ScheduleCronJobModel.builder()
                .jobName("Health_Check_Job")
                .jobGroup("Health_Check_Job")
                .jobClass(JavaWebServiceHealthCheckJob.class)
                .triggerName("Health_Check_Trigger")
                .triggerGroup("Health_Check_Trigger")
                .cronExpression("0 */1 * * * ?")
                .build();
        JobUtils.scheduleCronJob(scheduleCronJobModel);
    }
}
