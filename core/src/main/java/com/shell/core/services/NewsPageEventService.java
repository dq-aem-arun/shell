package com.shell.core.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = NewsPageEventService.class, immediate = true)
public class NewsPageEventService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsPageEventService.class);
    private static final String JOB_TOPIC = "com/shell/core/news/event-logging";

    private final ConcurrentHashMap<String, String> titleCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> authorCache = new ConcurrentHashMap<>();
    private final Set<String> processedPaths = ConcurrentHashMap.newKeySet();
    private final List<Map<String, String>> createdEvents = new ArrayList<>();
    private final List<Map<String, String>> updatedEvents = new ArrayList<>();
    private final List<Map<String, String>> deletedEvents = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Object lock = new Object();
    private ScheduledFuture<?> scheduledLogTask;

    @Reference
    private JobManager jobManager;

    public void testMethod(String from) {
        LOG.info("{}, test method", from);
    }

    public boolean isDuplicate(String pagePath) {
        if (processedPaths.contains(pagePath)) {
            return true;
        }
        processedPaths.add(pagePath);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                processedPaths.remove(pagePath);
            }
        }, 2000);
        return false;
    }

    public String getPagePath(String eventPath, String NEWS_PATH) {
        if (eventPath == null || !eventPath.startsWith(NEWS_PATH))
            return null;
        int jcrIndex = eventPath.indexOf("/jcr:content");
        if (jcrIndex > 0)
            return eventPath.substring(0, jcrIndex);
        String[] parts = eventPath.split("/");
        if (parts.length >= 7)
            return String.join("/", Arrays.copyOfRange(parts, 0, 7));
        return null;
    }

    public String fetchPageTitle(String pagePath, Session session) {
        try {
            String contentPath = pagePath + "/jcr:content";
            if (session.nodeExists(contentPath)) {
                Node contentNode = session.getNode(contentPath);
                if (contentNode.hasProperty("jcr:title")) {
                    return contentNode.getProperty("jcr:title").getString();
                } else {
                    return contentNode.getParent().getName();
                }
            }
        } catch (RepositoryException e) {
            LOG.error("SHELL_LOG: Error fetching title for {}", pagePath, e);
        }
        return null;
    }

    public String fetchPageAuthor(String pagePath, Session session) {
        try {
            String contentPath = pagePath + "/jcr:content";
            if (session.nodeExists(contentPath)) {
                Node contentNode = session.getNode(contentPath);
                if (contentNode.hasProperty("jcr:createdBy")) {
                    return contentNode.getProperty("jcr:createdBy").getString();
                }
            }
        } catch (RepositoryException e) {
            LOG.error("SHELL_LOG: Error fetching author for {}", pagePath, e);
        }
        return "Unknown";
    }

    public void addToCreatedBatch(String title, String path, String author) {
        synchronized (lock) {
            Map<String, String> eventData = new HashMap<>();
            eventData.put("eventType", "CREATED");
            eventData.put("title", title != null ? title : "Unknown");
            eventData.put("path", path);
            eventData.put("author", author);
            eventData.put("date", getCurrentTimestamp());
            createdEvents.add(eventData);
            scheduleBatchJob();
        }
    }

    public void addToUpdatedBatch(String title, String path, String author) {
        synchronized (lock) {
            Map<String, String> eventData = new HashMap<>();
            eventData.put("eventType", "UPDATED");
            eventData.put("title", title != null ? title : "Unknown");
            eventData.put("path", path);
            eventData.put("author", author);
            eventData.put("date", getCurrentTimestamp());
            updatedEvents.add(eventData);
            scheduleBatchJob();
        }
    }

    public void addToDeletedBatch(String title, String path, String author) {
        synchronized (lock) {
            Map<String, String> eventData = new HashMap<>();
            eventData.put("eventType", "DELETED");
            eventData.put("title", title != null ? title : "Unknown");
            eventData.put("path", path);
            eventData.put("author", author);
            eventData.put("date", getCurrentTimestamp());
            deletedEvents.add(eventData);
            scheduleBatchJob();
        }
    }

    private void scheduleBatchJob() {
        synchronized (lock) {
            if (scheduledLogTask != null && !scheduledLogTask.isDone()) {
                scheduledLogTask.cancel(false);
            }
            scheduledLogTask = scheduler.schedule(() -> {
                try {
                    Map<String, Object> jobProperties = new HashMap<>();
                    synchronized (lock) {
                        if (!createdEvents.isEmpty()) {
                            jobProperties.put("createdEvents", new ArrayList<>(createdEvents));
                            createdEvents.clear();
                        }
                        if (!updatedEvents.isEmpty()) {
                            jobProperties.put("updatedEvents", new ArrayList<>(updatedEvents));
                            updatedEvents.clear();
                        }
                        if (!deletedEvents.isEmpty()) {
                            jobProperties.put("deletedEvents", new ArrayList<>(deletedEvents));
                            deletedEvents.clear();
                        }
                    }
                    if (!jobProperties.isEmpty()) {
                        Job job = jobManager.addJob(JOB_TOPIC, jobProperties);
                        if (job == null) {
                            LOG.error("SHELL_LOG: Failed to add job for topic {}", JOB_TOPIC);
                        } else {
                            LOG.debug("SHELL_LOG: Added job for topic {} with ID {}", JOB_TOPIC, job.getId());
                        }
                    }
                } catch (Exception e) {
                    LOG.error("SHELL_LOG: Error scheduling batch log", e);
                }
            }, 500, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isPageNode(Session session, String path) {
        try {
            if (session.nodeExists(path)) {
                Node node = session.getNode(path);
                return node.isNodeType("cq:Page");
            }
        } catch (RepositoryException e) {
            LOG.error("SHELL_LOG: Error checking if node is cq:Page for path {}", path, e);
        }
        return false;
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void close() {
        scheduler.shutdown();
    }

    public void updateCaches(String path, String title, String author) {
        if (title != null) {
            titleCache.put(path, title);
        }
        authorCache.put(path, author);
    }

    public ConcurrentHashMap<String, String> getTitleCache() {
        return titleCache;
    }

    public ConcurrentHashMap<String, String> getAuthorCache() {
        return authorCache;
    }
}