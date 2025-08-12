package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.shell.core.models.FooterItem;

@Model(adaptables = Resource.class, adapters = FooterItem.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterItemImpl implements FooterItem {
    @ValueMapValue
    private String title;

    @ValueMapValue
    private String path;

    @ValueMapValue(name = "leftFileReference")
    private String leftImage;

    @ValueMapValue(name = "rightFileReference")
    private String rightImage;

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getLeftImage() {
        return leftImage;
    }

    public String getRightImage() {
        return rightImage;
    }


}
