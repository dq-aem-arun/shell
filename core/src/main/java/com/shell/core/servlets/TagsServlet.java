package com.shell.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/shell/dynamicdropdown/tags",
                "sling.servlet.methods=GET"
        })
public class TagsServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");

        if (category == null || subcategory == null) return;

        ResourceResolver resolver = request.getResourceResolver();
        String path = "/content/shell/us/en/data/categories/" + category + "/subcategories/" + subcategory + "/tags";
        Resource subCatRoot = resolver.getResource(path);

        JsonArray result = new JsonArray();
        if (subCatRoot != null) {
            for (Resource tag : subCatRoot.getChildren()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("value", tag.getName());
                obj.addProperty("text", tag.getValueMap().get("jcr:title", tag.getName()));
                result.add(obj);
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}
