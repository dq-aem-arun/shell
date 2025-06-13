//package com.shell.core.services;
//
//import com.shell.core.config.MySchedulerConfig;
//import com.shell.core.services.MySchedulerService;
//
//import com.day.cq.wcm.api.Page;
//import org.apache.sling.api.resource.*;
//import org.osgi.service.component.annotations.*;
//import org.osgi.service.metatype.annotations.Designate;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//
//@Component(
//        service = {Runnable.class, MySchedulerService.class},
//        configurationPolicy = ConfigurationPolicy.REQUIRE
//)
//@Designate(ocd = MySchedulerConfig.class)
//public class MySchedulerServiceImpl implements MySchedulerService, Runnable {
//
//    private static final Logger log = LoggerFactory.getLogger(MySchedulerServiceImpl.class);
//
//    @Reference
//    private ResourceResolverFactory resolverFactory;
//
//    private boolean enabled;
//    private String cron;
//    private String apiUrl;
//
//    private static final String SYSTEM_USER = "datawrite"; // Define in Sling User Mapper
//
//    @Activate
//    @Modified
//    protected void activate(MySchedulerConfig config) {
//        this.enabled = config.enable();
//        this.cron = config.scheduler_expression();
//        this.apiUrl = config.apiUrl();
//
//        log.info("Scheduler config activated: enabled={}, cron={}, apiUrl={}", enabled, cron, apiUrl);
//    }
//
//    @Override
//    public void run() {
//        if (!enabled) {
//            log.info("Scheduler disabled.");
//            return;
//        }
//
//        log.info("Running scheduled task...");
//        executeScheduledTask();
//    }
//
//    @Override
//    public void executeScheduledTask() {
//        try {
//            Map<String, Object> map = new HashMap<>();
//            map.put(ResourceResolverFactory.SUBSERVICE, SYSTEM_USER);
//
//            try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(map)) {
//                String path = "/content/we-retail/us/en";
//                Resource resource = resolver.getResource(path);
//
//                if (resource != null) {
//                    Page page = resource.adaptTo(Page.class);
//                    Iterator<Page> children = page.listChildren();
//
//                    while (children.hasNext()) {
//                        Page child = children.next();
//                        log.info("Child Page Title: {}", child.getTitle());
//                    }
//                } else {
//                    log.warn("Page not found at {}", path);
//                }
//
//                // Example: Use apiUrl for some logic
//                log.info("API URL being used: {}", apiUrl);
//
//            }
//        } catch (Exception e) {
//            log.error("Error executing scheduled task", e);
//        }
//    }
//}
