package com.shell.core.servlets;


import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.dam.api.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.*;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/cfList",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET
        }
)
public class CardFilterListServlet extends SlingSafeMethodsServlet {

    private static final String PN_CFM_MODEL = "jcr:content/data/cq:model";

    private static final Logger LOG = LoggerFactory.getLogger(CardFilterListServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String parentPath = request.getParameter("parentPath");
        String modelPath  = request.getParameter("modelPath");

        if (StringUtils.isBlank(parentPath) || StringUtils.isBlank(modelPath)) {
            response.setStatus(400);
            response.getWriter().write("{\"error\":\"parentPath and modelPath are required\"}");
            return;
        }
        LOG.info("CardFilterListServlet called with parentPath: {}, modelPath: {}", parentPath, modelPath);
        ResourceResolver resolver = request.getResourceResolver();
        Iterator<Resource> it = resolver.findResources(
                "SELECT * FROM [dam:Asset] AS s WHERE ISDESCENDANTNODE(s, '" + parentPath + "')",
                "JCR-SQL2"
        );

        List<Map<String, Object>> items = new ArrayList<>();

        while (it.hasNext()) {
            Resource assetRes = it.next();
            String cfModel = assetRes.getValueMap().get(PN_CFM_MODEL, String.class);
            if (!modelPath.equals(cfModel)) continue;

            ContentFragment cf = assetRes.adaptTo(ContentFragment.class);
            Asset asset = assetRes.adaptTo(Asset.class);
            if (cf == null || asset == null) continue;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("title", get(cf, "title"));
            row.put("subTitle", get(cf, "subTitle"));
            row.put("tileName", get(cf, "tileName"));
            row.put("description", get(cf, "description"));
            row.put("productImage", get(cf, "productImage"));

            row.put("pathName1", get(cf, "pathName1"));
            row.put("linkPath1", get(cf, "linkPath1"));
            row.put("linkImageRight1", get(cf, "linkImageRight1"));
            row.put("linkImageLeft1", get(cf, "linkImageLeft1"));

            row.put("pathName2", get(cf, "pathName2"));
            row.put("linkPath2", get(cf, "linkPath2"));
            row.put("linkImageRight2", get(cf, "linkImageRight2"));
            row.put("linkImageLeft2", get(cf, "linkImageLeft2"));

            row.put("mainLinkName", get(cf, "mainLinkName"));
            row.put("mainLinkPath", get(cf, "mainLinkPath"));
            row.put("mainLinkImage", get(cf, "mainLinkImage"));

            items.add(row);
        }

        LOG.info("Found {} items for parentPath: {}, modelPath: {}", items.size(), parentPath, modelPath);
        LOG.info("Items: {}", items);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), items);
    }

    private String get(ContentFragment cf, String elementName) {
        try {
            ContentElement el = cf.getElement(elementName);
            if (el != null && el.getValue() != null) {
                return el.getValue().getValue(String.class);
            }
        } catch (Exception ignored) {}
        return "";
    }
}
