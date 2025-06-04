package com.shell.core.servlets;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 *  @author Mohammad Shoaib
 *  @version v1.0
 *  @since 04-06-2025
 *
 * Servlet to retrieve and return the list of tags from AEM in a nested JSON format.
 * The servlet is registered at the path /bin/tags/list.
 *
 * Example response:
 * [
 *   {
 *     "title": "Shell",
 *     "path": "/content/cq:tags/shell",
 *     "tagId": "shell",
 *     "children": [...]
 *   }
 * ]
 */
@Component(service = Servlet.class)
@SlingServletPaths("/bin/tags/list")
public class TagsList extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(TagsList.class);

    /**
     * Handles GET requests to return a hierarchical list of tags under /content/cq:tags.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws IOException in case of I/O error
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        LOG.info("TagsList servlet invoked to fetch tags");

        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

            if (tagManager == null) {
                LOG.error("TagManager could not be adapted from ResourceResolver");
                sendErrorResponse(response, "Unable to get TagManager");
                return;
            }

            Tag rootTag = tagManager.resolve("/content/cq:tags/shell");
            if (rootTag == null) {
                LOG.error("Root tag '/content/cq:tags' could not be resolved");
                sendErrorResponse(response, "Root tag not found");
                return;
            }

            JSONArray result = new JSONArray();
            Iterator<Tag> children = rootTag.listChildren();
            while (children.hasNext()) {
                Tag child = children.next();
                result.put(buildTagTree(child));
            }

            LOG.info("Successfully retrieved tag hierarchy with {} root children", result.length());

            PrintWriter out = response.getWriter();
            out.write(result.toString());
            out.flush();

        } catch (Exception e) {
            LOG.error("Error while retrieving tags", e);
            sendErrorResponse(response, "Error retrieving tags: " + e.getMessage());
        }
    }

    /**
     * Recursively builds a JSON object representing a tag and its children.
     *
     * @param tag the current tag
     * @return JSONObject representing the tag
     * @throws JSONException if JSON creation fails
     */
    private JSONObject buildTagTree(Tag tag) throws JSONException {
        LOG.debug("Building tag tree for tag: {}", tag.getPath());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", tag.getTitle());
        jsonObject.put("path", tag.getPath());
        jsonObject.put("tagId", tag.getTagID());

        JSONArray childrenArray = new JSONArray();
        Iterator<Tag> children = tag.listChildren();
        while (children.hasNext()) {
            childrenArray.put(buildTagTree(children.next()));
        }

        if (childrenArray.length() > 0) {
            jsonObject.put("children", childrenArray);
        }

        return jsonObject;
    }

    /**
     * Sends an error response in JSON format with success=false.
     *
     * @param response the Sling HTTP response
     * @param message  the error message
     * @throws IOException in case of I/O error
     */
    private void sendErrorResponse(SlingHttpServletResponse response, String message) throws IOException {
        LOG.warn("Sending error response: {}", message);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", message);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);

        PrintWriter out = response.getWriter();
        out.write(jsonResponse);
        out.flush();
    }
}
