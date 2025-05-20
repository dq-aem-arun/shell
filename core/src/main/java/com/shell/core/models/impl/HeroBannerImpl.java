package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.shell.core.models.HeroBanner;

@Model(
    adaptables = Resource.class,
    adapters = HeroBanner.class,
    resourceType = HeroBanner.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroBannerImpl implements HeroBanner {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String details;

    @ValueMapValue
    private String image;

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getImage() {
        return image;
    }
}
