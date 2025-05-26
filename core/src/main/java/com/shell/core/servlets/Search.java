package com.shell.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.jcr.Session;

@Component(service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/search",
        "sling.servlet.methods=GET"
    })
public class Search extends SlingAllMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String searchTerm = request.getParameter("searchTerm");

        Map<String, String> map = new HashMap<>();
        map.put("path", "/content/shell");
        map.put("type", "cq:Page");
        map.put("fulltext", searchTerm);
        map.put("p.limit", "10");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map),
                request.getResourceResolver().adaptTo(Session.class));
        SearchResult result = query.getResult();

        JSONArray jsonArray = new JSONArray();
        ResourceResolver resolver = request.getResourceResolver();

        for (Hit hit : result.getHits()) {
            try {
                JSONObject json = new JSONObject();
                String path = hit.getPath();
                String title = hit.getTitle() != null ? hit.getTitle() : hit.getPath().substring(hit.getPath().lastIndexOf('/') + 1);
                String excerpt = hit.getExcerpt();

                // Highlight search term in excerpt
                if (excerpt != null && searchTerm != null && !searchTerm.isEmpty()) {
                    excerpt = excerpt.replaceAll("(?i)(" + searchTerm + ")", "<strong>$1</strong>");
                }

                // Breadcrumb logic
                Resource pageResource = resolver.getResource(path);
                StringBuilder breadcrumb = new StringBuilder();
                if (pageResource != null) {
                    List<String> breadcrumbParts = new LinkedList<>();
                    Resource current = pageResource;
                    while (current != null && !"/content/shell/us/en".equals(current.getPath())) {
                        String currentTitle = current.getValueMap().get("jcr:title", current.getName());
                        breadcrumbParts.add(0, currentTitle);
                        current = current.getParent();
                    }
                    breadcrumbParts.add(0, "Home");
                    breadcrumb.append(String.join(" > ", breadcrumbParts));
                }

                json.put("title", title);
                json.put("path", path);
                json.put("breadcrumb", breadcrumb.toString());
                json.put("excerpt", excerpt != null ? excerpt : "");
                json.put("score", hit.getScore()); // optional
                json.put("description", hit.getProperties().get("jcr:description", "")); // optional
                json.put("lastModified", hit.getProperties().get("cq:lastModified", "")); // optional
                json.put("resourceType", hit.getResource().getResourceType()); // optional

                jsonArray.put(json);
            } catch (Exception e) {
                // Logging omitted
            }
        }

        // Clean JSON output to unescape characters
        response.setContentType("application/json");
        String responseJson = jsonArray.toString()
                .replace("\\u003E", ">")
                .replace("\\u003C", "<")
                .replace("\\u0026", "&");
        response.getWriter().write(responseJson);
    }
}
