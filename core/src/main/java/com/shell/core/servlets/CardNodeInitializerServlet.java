package com.shell.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Custom Card Container Node Initializer Servlet",
        "sling.servlet.resourceTypes=shell/components/content/customcardcontainer",
        "sling.servlet.methods=POST",
        "sling.servlet.selectors=dynamic",
        "sling.servlet.extensions=json"
})
public class CardNodeInitializerServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(CardNodeInitializerServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        log.info(" [Servlet] CardNodeInitializerServlet triggered");

        Resource resource = request.getResource();
        String resourcePath = resource != null ? resource.getPath() : "null";

        log.info("[Servlet] Resource path from request: {}", resourcePath);
        log.info("[Servlet] Resource type: {}", resource.getResourceType());
        log.info("[Servlet] Selector: {}", request.getRequestPathInfo().getSelectorString());
        log.info("[Servlet] Extension: {}", request.getRequestPathInfo().getExtension());
        log.info("[Servlet] Method: {}", request.getMethod());

        if (!"shell/components/content/customcardcontainer".equals(resource.getResourceType())) {
            log.warn(" [Servlet] Resource type mismatch. Servlet won't proceed.");
            response.setStatus(400);
            response.getWriter().write("Invalid resource type: " + resource.getResourceType());
            return;
        }

        Node componentNode = resource.adaptTo(Node.class);
        if (componentNode == null) {
            log.error(" [Servlet] Cannot adapt resource to Node: {}", resourcePath);
            response.setStatus(500);
            response.getWriter().write("Unable to process component node.");
            return;
        }

        // Get numberOfCards from request
        String numberOfCardsStr = request.getParameter("numberOfCards");
        int numberOfCards;

        try {
            numberOfCards = Integer.parseInt(numberOfCardsStr);
            if (numberOfCards < 2 || numberOfCards > 8) {
                log.warn(" [Servlet] Invalid numberOfCards '{}'. Defaulting to 2", numberOfCardsStr);
                numberOfCards = 2;
            }
        } catch (NumberFormatException e) {
            log.warn(" [Servlet] numberOfCards format invalid: '{}'. Defaulting to 2", numberOfCardsStr);
            numberOfCards = 2;
        }

        try {
            // Count existing card nodes with prefix "card_"
            int existingCount = 0;
            NodeIterator iterator = componentNode.getNodes("card_*");

            while (iterator.hasNext()) {
                iterator.nextNode();
                existingCount++;
            }

            log.info("[Servlet] Existing cards: {}, Requested cards: {}", existingCount, numberOfCards);

            // Add missing cards
            if (numberOfCards > existingCount) {
                for (int i = existingCount + 1; i <= numberOfCards; i++) {
                    String nodeName = "card_" + i;
                    if (!componentNode.hasNode(nodeName)) {
                        Node newNode = componentNode.addNode(nodeName, "nt:unstructured");
                        newNode.setProperty("sling:resourceType", "shell/components/ctacard");
                        log.info(" [Servlet] Added card node: {}", nodeName);
                    }
                }
            }

            // Remove extra cards
            if (numberOfCards < existingCount) {
                for (int i = numberOfCards + 1; i <= existingCount; i++) {
                    String nodeName = "card_" + i;
                    if (componentNode.hasNode(nodeName)) {
                        componentNode.getNode(nodeName).remove();
                        log.info(" [Servlet] Removed card node: {}", nodeName);
                    }
                }
            }

            componentNode.getSession().save();
            log.info(" [Servlet] Node update complete. Final card count: {}", numberOfCards);
            response.setStatus(200);
            response.getWriter().write("Successfully updated cards to count: " + numberOfCards);

        } catch (RepositoryException e) {
            log.error(" [Servlet] Repository exception", e);
            response.setStatus(500);
            response.getWriter().write("Error updating card nodes: " + e.getMessage());
        }
    }

}
