package com.shell.core.servlets;

import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import  org.apache.sling.api.request.RequestPathInfo;
class FetchPageTagsServletTest {

    private FetchPageTagsServlet servlet;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resourceResolver;
    private PageManager pageManager;
    private TagManager tagManager;
    private Page page;
    private Resource contentResource;
    private ValueMap valueMap;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new FetchPageTagsServlet();
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resourceResolver = mock(ResourceResolver.class);
        pageManager = mock(PageManager.class);
        tagManager = mock(TagManager.class);
        page = mock(Page.class);
        contentResource = mock(Resource.class);
        valueMap = mock(ValueMap.class);

        when(request.getParameter("path")).thenReturn("/content/shell");
        when(request.getParameter("tagName")).thenReturn("shell:oil");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
        when(pageManager.getPage("/content/shell")).thenReturn(page);
        when(page.getPath()).thenReturn("/content/shell");
        when(page.getContentResource()).thenReturn(contentResource);
        when(contentResource.getValueMap()).thenReturn(valueMap);
        when(valueMap.containsKey("cq:tags")).thenReturn(true);
        when(valueMap.get("cq:tags", String[].class)).thenReturn(new String[]{"shell:oil", "shell:energy"});

        Iterator<Page> emptyIterator = Collections.emptyIterator();
        when(page.listChildren()).thenReturn(emptyIterator);

        responseWriter = new StringWriter();

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }


    @Test
    void testDoGet_withValidPathAndTags() throws Exception, JSONException {
        RequestPathInfo pathinfo = request.getRequestPathInfo();
        servlet.doGet(request, response);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        String jsonResponse = responseWriter.toString();
        JSONObject json = new JSONObject(jsonResponse);
        assertTrue(json.has("pages"));
        assertTrue(json.has("tagSummary"));
        assertTrue(json.has("totalPages"));
        assertTrue(json.has("taggedPages"));
        assertTrue(json.has("pagesWithTag"));
        assertTrue(json.getJSONArray("pages").length() > 0);
        assertTrue(json.getJSONObject("tagSummary").has("shell:oil"));
        writer.flush();
    }

    @Test
    void testDoGet_withMissingPathParam() throws Exception {
        when(request.getParameter("path")).thenReturn(null);

        servlet.doGet(request, response);
        String output = responseWriter.toString();

        assertTrue(output.contains("Missing required 'path' parameter"));
    }

    @Test
    void testDoGet_withInvalidPath() throws Exception {
        when(request.getParameter("path")).thenReturn("/invalid/path");
        when(pageManager.getPage("/invalid/path")).thenReturn(null);

        servlet.doGet(request, response);
        String output = responseWriter.toString();

        assertTrue(output.contains("Invalid path"));
    }
}
