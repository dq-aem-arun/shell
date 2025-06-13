package com.shell.core.servlets;

import com.shell.core.services.JsonAPIDetails;
import org.json.JSONObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;


@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Details Servlet",
                "sling.servlet.paths=/bin/fetch-details",
                "sling.servlet.methods=POST"
        }
)
public class DetailsServlet extends SlingAllMethodsServlet {

    @Reference
    private JsonAPIDetails jsonAPIDetails;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        JSONObject jsonObject = jsonAPIDetails.fetchJsonDetailsById(id);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObject.toString());
    }
}
