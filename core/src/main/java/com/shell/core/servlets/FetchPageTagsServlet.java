package com.shell.core.servlets;

import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/fetch-page-tags",
                "sling.servlet.methods=GET"

        })
public class FetchPageTagsServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String path = request.getParameter("path");
        String tagFilter = request.getParameter("tagName");

        JSONObject result = new JSONObject();

        if (StringUtils.isBlank(path)) {
            try {
                result.put("error", "Missing required 'path' parameter");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().write(result.toString());
            return;
        }

        try (ResourceResolver resolver = request.getResourceResolver()) {
            PageManager pageManager = resolver.adaptTo(PageManager.class);//pages
            TagManager tagManager = resolver.adaptTo(TagManager.class);//tags

            if (pageManager == null || tagManager == null) {
                result.put("error", "PageManager or TagManager unavailable");
                response.getWriter().write(result.toString());
                return;
            }

            Page rootPage = pageManager.getPage(path);//root page form page manager
            if (rootPage == null) {
                result.put("error", "Invalid path: " + path);
                response.getWriter().write(result.toString());
                return;
            }

            JSONArray pagesArray = new JSONArray();
            Map<String, Integer> tagCounts = new HashMap<>();
            AtomicInteger filteredTagCount = new AtomicInteger(0);
            AtomicInteger totalPages = new AtomicInteger(0);;
            AtomicInteger taggedPages = new AtomicInteger(0);;

            traverseAndCollectPages(rootPage, pagesArray, tagCounts, tagFilter, filteredTagCount, totalPages, taggedPages);
            // int pagesres=totalPages-1;
            //incrementAndGet();
            result.put("pages", pagesArray);
            result.put("tagSummary", new JSONObject(tagCounts));
            result.put("totalPages", totalPages);
            result.put("taggedPages", taggedPages);
            if (tagFilter != null) {
                result.put("pagesWithTag", filteredTagCount);
            }

        } catch (Exception e) {
            try {
                result.put("error", "Exception occurred: " + e.getMessage());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        response.getWriter().write(result.toString());
    }

    private void traverseAndCollectPages(Page page,
                                         JSONArray pagesArray,
                                         Map<String, Integer> tagCounts,
                                         String tagFilter,
                                         AtomicInteger filteredTagCount,
                                         AtomicInteger totalPages,
                                         AtomicInteger taggedPages) throws JSONException {

        Resource contentRes = page.getContentResource();
        JSONObject pageJson = new JSONObject();
        JSONObject result = new JSONObject();


        try {

            totalPages.incrementAndGet(); // increment total page count
            pageJson.put("pagePath", page.getPath());

            if (contentRes != null && contentRes.getValueMap().containsKey("cq:tags")) {
                String[] tags = contentRes.getValueMap().get("cq:tags", String[].class);
                JSONArray tagArray = new JSONArray();

                boolean hasFilteredTag = false;

                if (tags != null && tags.length > 0) {
                    taggedPages.incrementAndGet(); // increment tagged page count

                    for (String tag : tags) {
                        tagArray.put(tag);
                        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                        if (tagFilter != null && tag.equals(tagFilter)) {
                            hasFilteredTag = true;
                        }
                    }

                    if (hasFilteredTag) {
                        filteredTagCount.incrementAndGet();
                    }
                }

                pageJson.put("tags", tagArray);
            } else {
                pageJson.put("tags", new JSONArray());
            }

            pagesArray.put(pageJson);

            Iterator<Page> children = page.listChildren();
            while (children.hasNext()) {
                traverseAndCollectPages(children.next(), pagesArray, tagCounts, tagFilter, filteredTagCount, totalPages, taggedPages);
            }
        } catch (Exception e) {
            result.put("error", "Exception occurred: " + e.getMessage());
        }
    }
}