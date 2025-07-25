package com.shell.core.jobs;

import com.shell.core.services.EmailNotificationService;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Component(
        service = JobConsumer.class,
        immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=com/shell/core/news/event-logging"
        }
)
public class NewsPageEventJobConsumer implements JobConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(NewsPageEventJobConsumer.class);

    @Reference
    private EmailNotificationService emailNotificationService;

    @Override
    public JobResult process(Job job) {
        try {
            List<Map<String, String>> createdEvents = (List<Map<String, String>>) job.getProperty("createdEvents");
            List<Map<String, String>> updatedEvents = (List<Map<String, String>>) job.getProperty("updatedEvents");
            List<Map<String, String>> deletedEvents = (List<Map<String, String>>) job.getProperty("deletedEvents");

            if (createdEvents != null && !createdEvents.isEmpty()) {
                LOG.info("SHELL_LOG page Created:  {}",createdEvents);
                emailNotificationService.sendEmail(createdEvents,"Created");
            }

            if (updatedEvents != null && !updatedEvents.isEmpty()) {
                LOG.info("SHELL_LOG Page Updated:{}",updatedEvents);
                emailNotificationService.sendEmail(updatedEvents,"Updated");
            }

            if (deletedEvents != null && !deletedEvents.isEmpty()) {
                LOG.info("SHELL_LOG Deleted:  {}", deletedEvents);
                emailNotificationService.sendEmail(deletedEvents,"Deleted");
            }

            return JobResult.OK;

        } catch (Exception e) {
            LOG.error("SHELL_LOG: Error processing job {}", job.getId(), e);
            return JobResult.FAILED;
        }
    }
}