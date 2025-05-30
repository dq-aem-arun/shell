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

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

            if (tagManager == null) {
                sendErrorResponse(response, "Unable to get TagManager");
                return;
            }

            Tag resolve = tagManager.resolve("/content/cq:tags/shell/technology");
            if (resolve != null) {
                Iterator<Tag> listChildren = resolve.listChildren();

                JSONArray jsonArray = new JSONArray();

                while (listChildren.hasNext()) {
                    Tag next = listChildren.next();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", next.getTitle());
                    jsonObject.put("path", next.getPath());

                    jsonArray.put(jsonObject);
                }
                PrintWriter out = response.getWriter();
                out.write(jsonArray.toString());
                out.flush();
            }


        } catch (Exception e) {
            LOG.error("Error retrieving tags", e);
            sendErrorResponse(response, "Error retrieving tags: " + e.getMessage());
        }


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
