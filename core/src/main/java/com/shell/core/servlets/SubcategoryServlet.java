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
                "sling.servlet.paths=/bin/shell/dynamicdropdown/subcategories",
                "sling.servlet.methods=GET"
        })
public class SubcategoryServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String category = request.getParameter("category");
        if (category == null) return;

        ResourceResolver resolver = request.getResourceResolver();
        Resource catRoot = resolver.getResource("/content/shell/data/categories/" + category + "/subcategories");

        JsonArray result = new JsonArray();
        if (catRoot != null) {
            for (Resource child : catRoot.getChildren()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("value", child.getName());
                obj.addProperty("text", child.getValueMap().get("jcr:title", child.getName()));
                result.add(obj);
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}
