package com.shell.core.services;

import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;

import java.util.*;

@Component(service = BreadcrumbService.class)
public class BreadcrumbService {

    private static final String STOP_PATH = "/content/shell/us/en";

    /**
     * Builds breadcrumb as a list of maps with "title" and "path" keys.
     */
    public List<Map<String, String>> buildBreadcrumb(Resource pageResource) {
        List<Map<String, String>> breadcrumbList = new LinkedList<>();

        if (pageResource == null) {
            return breadcrumbList;
        }

        Resource current = pageResource;

        while (current != null && !STOP_PATH.equals(current.getPath())) {
            String title = current.getValueMap().get("jcr:title", current.getName());
            String path = current.getPath();

            Map<String, String> crumb = new HashMap<>();
            crumb.put("title", title);
            crumb.put("path", path);

            breadcrumbList.add(0, crumb); // add to beginning
            current = current.getParent();
        }

        // Add "Home" manually
        Map<String, String> home = new HashMap<>();
        home.put("title", "Home");
        home.put("path", STOP_PATH); // or "/", depending on your routing
        breadcrumbList.add(0, home);

        return breadcrumbList;
    }
}
