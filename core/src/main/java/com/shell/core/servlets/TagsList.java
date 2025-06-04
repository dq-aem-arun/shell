package com.shell.core.servlets;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
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
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/tags/list")
public class TagsList extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(TagsList.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

            if (tagManager == null) {
                sendErrorResponse(response, "Unable to get TagManager");
                return;
            }

            Tag rootTag = tagManager.resolve("/content/cq:tags");
            if (rootTag == null) {
                sendErrorResponse(response, "Root tag not found");
                return;
            }

            JSONArray result = new JSONArray();
            Iterator<Tag> children = rootTag.listChildren();
            while (children.hasNext()) {
                Tag child = children.next();
                result.put(buildTagTree(child));
            }

            PrintWriter out = response.getWriter();
            out.write(result.toString());
            out.flush();

        } catch (Exception e) {
            LOG.error("Error retrieving tags", e);
            sendErrorResponse(response, "Error retrieving tags: " + e.getMessage());
        }
    }

    private JSONObject buildTagTree(Tag tag) throws JSONException {
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

    private void sendErrorResponse(SlingHttpServletResponse response, String message) throws IOException {
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
