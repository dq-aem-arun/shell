package com.shell.core.servlets;

// import com.adobe.cq.wcm.core.components.models.List; // Removed to avoid type conflict
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.shell.core.models.Search;
import com.shell.core.services.BreadcrumbService;
import com.shell.core.services.SearchService;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.json.JSONArray;
import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/search",
                "sling.servlet.methods=GET"
        })
public class SearchServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SearchServlet.class);

    @Reference
    private SearchService searchService;

    @Reference
    private BreadcrumbService breadcrumbService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String searchTerm = request.getParameter("searchTerm");
        String componentPath = request.getParameter("componentPath");

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("[]");
            LOG.info("No searchTerm provided, returning empty result.");
            return;
        }

        LOG.info("Received searchTerm: {}", searchTerm);
        LOG.info("Received componentPath: {}", componentPath);

        String rootPath = "/content/shell";
        int maxResults = 100;

        if (componentPath != null && !componentPath.isEmpty()) {
            Resource componentResource = request.getResourceResolver().getResource(componentPath);
            if (componentResource != null) {
                LOG.info("Component resource found at path: {}", componentPath);
                Search searchModel = componentResource.adaptTo(Search.class);
                if (searchModel != null) {
                    LOG.info("Adapted to Search model successfully.");
                    rootPath = searchModel.getRootPath();
                    maxResults = searchModel.getMaxResults();
                    LOG.info("Using rootPath from model: {}", rootPath);
                    LOG.info("Using maxResults from model: {}", maxResults);
                } else {
                    LOG.error("Could not adapt resource at {} to Search model", componentPath);
                }
            } else {
                LOG.error("Component resource not found at path: {}", componentPath);
            }
        }

        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", rootPath);
        predicates.put("type", "cq:Page");
        predicates.put("fulltext", searchTerm);
        predicates.put("p.limit", String.valueOf(maxResults));

        LOG.debug("Query predicates: {}", predicates);

        Session session = request.getResourceResolver().adaptTo(Session.class);
        SearchResult result = searchService.search(predicates, session);

        JSONArray jsonArray = new JSONArray();
        ResourceResolver resolver = request.getResourceResolver();

        for (Hit hit : searchService.getHits(result)) {
            try {
                JSONObject json = new JSONObject();
                String path = hit.getPath();
                String title = hit.getTitle() != null ? hit.getTitle() : path.substring(path.lastIndexOf('/') + 1);
                String excerpt = hit.getExcerpt();

                if (excerpt != null && searchTerm != null && !searchTerm.isEmpty()) {
                    excerpt = excerpt.replaceAll("(?i)(" + searchTerm + ")", "<strong>$1</strong>");
                }

                Resource pageResource = resolver.getResource(path);
                List<Map<String, String>> breadcrumb = breadcrumbService.buildBreadcrumb(pageResource);

                json.put("title", title);
                json.put("path", path);
                json.put("breadcrumb", breadcrumb);
                json.put("excerpt", excerpt != null ? excerpt : "");
                json.put("score", hit.getScore());
                json.put("description", hit.getProperties().get("jcr:description", ""));
                json.put("lastModified", hit.getProperties().get("cq:lastModified", ""));
                json.put("resourceType", hit.getResource().getResourceType());

                jsonArray.put(json);
            } catch (Exception e) {
                LOG.error("Error processing search hit", e);
            }
        }

        response.setContentType("application/json");
        String responseJson = jsonArray.toString()
                .replace("\\u003E", ">")
                .replace("\\u003C", "<")
                .replace("\\u0026", "&");

        response.getWriter().write(responseJson);
        LOG.debug("Search response sent with {} results.", jsonArray.length());
    }
}
