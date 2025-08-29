package com.shell.core.servlets;

import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.shell.core.services.BreadcrumbService;
import com.shell.core.services.SearchService;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.json.JSONArray;
import org.json.JSONException;
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

@Component(service = Servlet.class, property = {
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
            response.getWriter().write("{\"results\":[],\"totalResults\":0}");
            LOG.info("No searchTerm provided, returning empty result.");
            return;
        }

        LOG.info("Received searchTerm: {}", searchTerm);
        LOG.info("Received componentPath: {}", componentPath);

        String rootPath = "/content/shell";
        int maxResults = 10; // default fallback

        if (componentPath != null && !componentPath.isEmpty()) {
            Resource componentResource = request.getResourceResolver().getResource(componentPath);
            if (componentResource != null) {
                LOG.info("Component resource found at path: {}", componentPath);

                ValueMap properties = componentResource.getValueMap();
                rootPath = properties.get("rootPath", "/content");
                String maxResultsParam = properties.get("maxResults", "100");
                try {
                    maxResults = Integer.parseInt(maxResultsParam);
                } catch (NumberFormatException e) {
                    LOG.warn("Invalid maxResults value '{}', using default {}", maxResultsParam, maxResults);
                }

                LOG.info("Using rootPath from dialog: {}", rootPath);
                LOG.info("Using maxResults from dialog: {}", maxResults);

            } else {
                LOG.error("Component resource not found at path: {}", componentPath);
            }
        }

        // ✅ Pagination parameters
        int offset = 0;
        try {
            offset = Integer.parseInt(request.getParameter("offset"));
        } catch (Exception e) {
            offset = 0;
        }

        int limit = maxResults;
        try {
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (Exception e) {
            // keep default
        }

        LOG.info("Limit received: {}, Offset: {}", limit, offset);


        int currentPage = (offset / limit) + 1;

        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", rootPath);
        predicates.put("type", "nt:unstructured");
        predicates.put("fulltext", searchTerm);
        predicates.put("property", "sling:resourceType");
        predicates.put("property.value", "shell/components/text");
        predicates.put("p.offset", String.valueOf(offset));
        predicates.put("p.limit", String.valueOf(limit));
        LOG.info("Executing search with predicates: {}", predicates);

        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (session == null) {
            LOG.error("Failed to adapt ResourceResolver to Session. Search cannot proceed.");
            return;
        }

        SearchResult result = searchService.search(predicates, session);
        if (result == null) {
            LOG.error("Search service returned null result.");
            return;
        }

        long totalResults = result.getTotalMatches();
        int totalPages = (int) Math.ceil((double) totalResults / limit);

        JSONArray resultsArray = new JSONArray();
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        List<Hit> hits = searchService.getHits(result);
        LOG.info("Number of hits returned by searchService: {}", hits.size());

        int hitIndex = 0;
        for (Hit hit : hits) {
            try {
                hitIndex++;
                String path = hit.getPath();
                String excerpt = hit.getExcerpt();

                if (excerpt != null && searchTerm != null && !searchTerm.isEmpty()) {
                    excerpt = excerpt.replaceAll("(?i)(" + searchTerm + ")", "<strong>$1</strong>");
                }

                Resource componentResource = resolver.getResource(path);
                Page containingPage = pageManager.getContainingPage(componentResource);

                String pageTitle = "";
                String pagePath = "";
                List<Map<String, String>> breadcrumb = null;

                if (containingPage != null) {
                    pageTitle = containingPage.getTitle() != null ? containingPage.getTitle()
                            : containingPage.getName();
                    pagePath = containingPage.getPath();
                    breadcrumb = breadcrumbService.buildBreadcrumb(containingPage.adaptTo(Resource.class));
                }

                JSONObject json = new JSONObject();
                json.put("title", pageTitle);
                json.put("path", pagePath);
                json.put("breadcrumb", breadcrumb);
                json.put("excerpt", excerpt != null ? excerpt : "");
                json.put("score", hit.getScore());
                json.put("description", hit.getProperties().get("jcr:description", ""));
                json.put("lastModified", hit.getProperties().get("cq:lastModified", ""));
                json.put("resourceType", hit.getResource().getResourceType());

                resultsArray.put(json);
            } catch (Exception e) {
                LOG.error("Error processing search hit #{}", hitIndex, e);
            }
        }

        // ✅ Wrap with pagination info
        try{
        JSONObject responseJson = new JSONObject();
        responseJson.put("results", resultsArray);
        responseJson.put("totalResults", totalResults);
        responseJson.put("currentPage", currentPage);
        responseJson.put("pageSize", limit);
        responseJson.put("totalPages", totalPages);
        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        LOG.info("Search response sent with {} results, page {}/{}.", resultsArray.length(), currentPage, totalPages);
    }
}
