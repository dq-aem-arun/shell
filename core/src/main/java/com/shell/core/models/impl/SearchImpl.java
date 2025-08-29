package com.shell.core.models.impl;

import com.shell.core.models.Search;
import com.adobe.cq.export.json.ComponentExporter;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = Resource.class,
    adapters = { Search.class, ComponentExporter.class },
    resourceType = "shell/components/content/search", // adjust as needed
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SearchImpl implements Search {

    @ValueMapValue
    @Default(values = "Search here...")
    private String placeholder;

    @ValueMapValue
    private String rootPath;

    @ValueMapValue
    private int maxResults;

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public String getExportedType() {
        return "shell/components/content/search";
    }
}
