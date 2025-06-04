/**
 * EmailServiceTest
 * 
 * @author Jay 
 * 
 * Servlet for testing the EmailNotificationService by triggering a test email.
 */
package com.shell.core.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.shell.core.services.EmailNotificationService;

@Component(
    service=Servlet.class,
    property = {
        "sling.servlet.paths=/services/emailServiceTest",
        "sling.servlet.methods=GET",
        "service.description=Email Service Test Servlet"
    }
)
public class EmailServiceTest extends SlingAllMethodsServlet{

    @Reference
    private EmailNotificationService emailNotificationService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            String testPath = "/content/mysite/news/test-page";
            String testTitle = "Test News Page";
            String testAction = "Created";
            String testUser = "test-user";

            emailNotificationService.sendEmail(testPath, testTitle, testAction, testUser, new Date());
            response.setContentType("text/plain");
            response.getWriter().write("Email triggered successfully.");
        } catch (Exception e) {
            response.getWriter().write("Error while sending email: " + e);
        }
    }
    
}
