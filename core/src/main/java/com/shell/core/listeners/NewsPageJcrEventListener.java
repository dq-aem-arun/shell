package com.shell.core.listeners;

import com.shell.core.services.NewsPageEventService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.jcr.*;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

@Component(service = EventListener.class, immediate = true)
public class NewsPageJcrEventListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(NewsPageJcrEventListener.class);
    private static final String NEWS_PATH = "/content/shell/us/en/news";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private NewsPageEventService eventService;

    private ResourceResolver resolver;
    private Session session;

    @Activate
    protected void activate() throws LoginException {
        LOG.info("SHELL_LOG: Activating NewsPageJcrEventListener");
        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(ResourceResolverFactory.SUBSERVICE, "news-service-user");
        resolver = resolverFactory.getServiceResourceResolver(authInfo);
        session = resolver.adaptTo(Session.class);

        try {
            session.getWorkspace().getObservationManager().addEventListener(
                    this,
                    Event.NODE_ADDED | Event.PROPERTY_CHANGED | Event.NODE_REMOVED,
                    NEWS_PATH,
                    true,
                    null,
                    null,
                    false);
            LOG.info("SHELL_LOG: Registered JCR EventListener for {}", NEWS_PATH);
        } catch (RepositoryException e) {
            LOG.error("SHELL_LOG: Failed to register JCR EventListener", e);
        }
    }

    @Deactivate
    protected void deactivate() {
        LOG.info("SHELL_LOG: Deactivating NewsPageJcrEventListener");

        try {
            if (session != null && session.getWorkspace() != null) {
                session.getWorkspace().getObservationManager().removeEventListener(this);
            }
        } catch (RepositoryException e) {
            LOG.error("SHELL_LOG: Error removing event listener", e);
        }

        if (resolver != null && resolver.isLive()) {
            resolver.close();
        }
        if (session != null) {
            session.logout();
        }

        eventService.close();
    }

    @Override
    public void onEvent(EventIterator events) {
        while (events.hasNext()) {
            Event event = events.nextEvent();
            try {
                String path = event.getPath();
                int type = event.getType();

                String pagePath = eventService.getPagePath(path, NEWS_PATH);
                if (pagePath == null || eventService.isDuplicate(pagePath)) {
                    continue;
                }

                if (type != Event.NODE_REMOVED && !eventService.isPageNode(session, pagePath)) {
                    continue;
                }


                if (type == Event.NODE_ADDED) {
                    String title = eventService.fetchPageTitle(pagePath, session);
                    String author = eventService.fetchPageAuthor(pagePath, session);
                    eventService.updateCaches(pagePath, title, author);
                    eventService.addToCreatedBatch(title, pagePath, author);

                } else if (type == Event.PROPERTY_CHANGED && path.endsWith("jcr:title")) {
                    String title = eventService.fetchPageTitle(pagePath, session);
                    String author = eventService.fetchPageAuthor(pagePath, session);
                    eventService.updateCaches(pagePath, title, author);
                    eventService.addToUpdatedBatch(title, pagePath, author);

                } else if (type == Event.NODE_REMOVED) {
                    String title = eventService.getTitleCache().getOrDefault(pagePath, "Unknown");
                    String author = eventService.getAuthorCache().getOrDefault(pagePath, "Unknown");
                    eventService.addToDeletedBatch(title, pagePath, author);
                }

            } catch (RepositoryException e) {
                LOG.error("SHELL_LOG: Error processing event", e);
            }
        }
    }
}