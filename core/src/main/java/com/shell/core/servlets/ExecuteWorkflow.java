/*package com.shell.core.servlets;


import com.adobe.xfa.ut.StringUtils;
import jdk.internal.loader.Resource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/bin/executeworkflow","shell/executeworkflow"}
)
public class ExecuteWorkflow extends SlingSafeMethodsServlet {

    private static final logger LOG = LoggerFactory.getLogger(ExecuteWorkflow.class);

    @Override
    protected void doGet(final SlingHttpServletRequest request, SlingHttpServletResponse response)
        throws ServletException IOException{

        final ResourceResolver resourceResolver = request.getResourceResolver();

        String payload = request.getRequestParameter("page").getString();
        try{
            if(StringUtils.isNotBlank(payload);
        }

    }
}*/
