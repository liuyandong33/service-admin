package build.dream.devops.jobs;

import build.dream.common.domains.admin.Host;
import build.dream.devops.constants.Constants;
import build.dream.devops.services.HostService;
import build.dream.devops.utils.HostUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@DisallowConcurrentExecution
public class HostHealthCheckJob implements Job {
    @Autowired
    private HostService hostService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Host> hosts = hostService.obtainAllHosts();
        for (Host host : hosts) {
            pingHost(host);
        }
    }

    public void pingHost(Host host) {
        int status = host.getStatus();
        try {
            if (HostUtils.ping(host.getIpAddress())) {
                if (status != Constants.HOST_STATUS_RUNNING) {
                    host.setStatus(Constants.HOST_STATUS_RUNNING);
                    hostService.updateHost(host);
                }
            } else {
                if (status != Constants.HOST_STATUS_STOPPED) {
                    host.setStatus(Constants.HOST_STATUS_STOPPED);
                    hostService.updateHost(host);
                }
            }
        } catch (IOException e) {
            if (status != Constants.HOST_STATUS_STOPPED) {
                host.setStatus(Constants.HOST_STATUS_STOPPED);
                hostService.updateHost(host);
            }
        }
    }
}
