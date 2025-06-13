package com.shell.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "shell/components/updateproperty",
        methods = "POST",
        extensions = "json"
)
public class UpdatePropertyServlet extends SlingAllMethodsServlet {

  @Override
  protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp)
          throws ServletException, IOException {

    ResourceResolver resolver = null;

    try {
      resolver = req.getResourceResolver();

      String body = req.getReader().lines().reduce("", (acc, line) -> acc + line);
      JSONObject json = new JSONObject(body);

      String pagePath = json.optString("path");

      if (pagePath == null || pagePath.isEmpty()) {
        resp.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Missing 'path' in request.");
        return;
      }

      Resource pageResource = resolver.getResource(pagePath + "/jcr:content");

      if (pageResource != null) {
        ModifiableValueMap map = pageResource.adaptTo(ModifiableValueMap.class);

        if (map != null) {
          map.put("updatedProperty", "updated from postman");
          resolver.commit();

          resp.setStatus(SlingHttpServletResponse.SC_OK);
          resp.getWriter().write("Property updated successfully.");
        } else {
          resp.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          resp.getWriter().write("Could not adapt to ModifiableValueMap.");
        }
      } else {
        resp.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("Page not found: " + pagePath);
      }
    } catch (Exception e) {
      resp.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.getWriter().write("Exception: " + e.getMessage());
    }
  }
}