package com.shell.core.models.impl;

import com.shell.core.models.Promobanner;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

@Model(adaptables = Resource.class, adapters = Promobanner.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class PromobannerImpl implements Promobanner{

    @ValueMapValue
    private String fileReference;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @Override
    public String getFileReference() {
        return fileReference;
    }
    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public String getDescription() {
        return description;
    }
}
