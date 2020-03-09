package build.dream.devops.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.mappers.CommonMapper;
import build.dream.devops.jobs.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class DevOpsServletContextListener extends BasicServletContextListener {
    @Autowired
    private JobScheduler jobScheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        super.previousInjectionBean(servletContextEvent.getServletContext(), CommonMapper.class);
        jobScheduler.scheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
