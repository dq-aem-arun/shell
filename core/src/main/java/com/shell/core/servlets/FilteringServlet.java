package com.shell.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_PATHS + "=/bin/filter-articles",
                SLING_SERVLET_METHODS + "=POST"
        }
)
public class FilteringServlet extends SlingAllMethodsServlet {

    private static final Logger logger = LoggerFactory.getLogger(FilteringServlet.class);
    private static final String BASE_PATH = "/content/shell";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonRequest = JsonParser.parseReader(reader).getAsJsonObject();
            ResourceResolver resolver = request.getResourceResolver();

            // Extract and validate tags
            List<String> inputTags = new ArrayList<>();
            if (jsonRequest.has("tags") && jsonRequest.get("tags").isJsonArray()) {
                jsonRequest.get("tags").getAsJsonArray().forEach(tag -> inputTags.add(tag.getAsString()));
            }

            List<String> validTags = new ArrayList<>();
            for (String tag : inputTags) {
                String tagPath = "/content/cq:tags/shell/" + tag;
                if (resolver.getResource(tagPath) != null) {
                    validTags.add("shell:" + tag);
                } else {
                    logger.warn("Invalid tag: {}", tag);
                }
            }

            if (!inputTags.isEmpty() && validTags.isEmpty()) {
                response.getWriter().write("[]");
                return;
            }

            // Parse date range
            Date startDate = null;
            Date endDate = null;
            try {
                if (jsonRequest.has("startDate") && !jsonRequest.get("startDate").getAsString().isEmpty()) {
                    startDate = DATE_FORMAT.parse(jsonRequest.get("startDate").getAsString());
                }
                if (jsonRequest.has("endDate") && !jsonRequest.get("endDate").getAsString().isEmpty()) {
                    endDate = DATE_FORMAT.parse(jsonRequest.get("endDate").getAsString());
                }
            } catch (ParseException e) {
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid date format. Expected yyyy-MM-dd\"}");
                return;
            }

            // Pagination
            int limit = Optional.ofNullable(request.getParameter("limit")).map(Integer::parseInt).orElse(25);
            int offset = Optional.ofNullable(request.getParameter("offset")).map(Integer::parseInt).orElse(0);

            Session session = resolver.adaptTo(Session.class);
            if (session == null) {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Unable to get JCR session\"}");
                return;
            }

            Map<String, String> predicates = new HashMap<>();
            predicates.put("path", BASE_PATH);
            predicates.put("type", "cq:Page");
            predicates.put("p.limit", String.valueOf(limit));
            predicates.put("p.offset", String.valueOf(offset));

            if (!validTags.isEmpty()) {
                predicates.put("tagid.property", "jcr:content/cq:tags");
                predicates.put("tagid.operation", "and");
                for (int i = 0; i < validTags.size(); i++) {
                    predicates.put("tagid." + (i + 1) + "_value", validTags.get(i));
                }
            }

            // Apply date range if provided
            if (startDate != null || endDate != null) {
                predicates.put("daterange.property", "jcr:content/cq:lastModified");

                if (startDate != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    predicates.put("daterange.lowerBound", DATE_FORMAT.format(cal.getTime()));
                    predicates.put("daterange.lowerOperation", ">=");
                }

                if (endDate != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(endDate);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    predicates.put("daterange.upperBound", DATE_FORMAT.format(cal.getTime()));
                    predicates.put("daterange.upperOperation", "<=");
                }
            }

            // Execute query
            Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
            SearchResult result = query.getResult();

            JSONArray articles = new JSONArray();
            for (Hit hit : result.getHits()) {
                String path = hit.getPath();
                Resource pageRes = resolver.getResource(path + "/jcr:content");
                if (pageRes != null) {
                    ValueMap vm = pageRes.getValueMap();
                    String title = vm.get("jcr:title", "Untitled");
                    JSONObject obj = new JSONObject();
                    obj.put("title", title);
                    obj.put("url", path);
                    articles.put(obj);
                }
            }

            response.setStatus(SlingHttpServletResponse.SC_OK);
            response.getWriter().write(articles.toString());

        } catch (Exception e) {
            logger.error("Error in FilteringServlet: ", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Internal server error\"}");
        }
    }
}
