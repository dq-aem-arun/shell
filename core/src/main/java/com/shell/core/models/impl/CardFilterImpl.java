package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.shell.core.models.CardFilter;

/**
 * Implementation of the CardFilter model interface.
 * This model is used to filter content fragments based on their parent path and model path.
 * 
 * @author Mohammad Shoaib
 * @version 1.0
 * @since 17-08-2025
 */
@Model(adaptables = Resource.class,
       adapters = CardFilter.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardFilterImpl implements CardFilter {

    @ValueMapValue
    private String parentPath;

    @ValueMapValue
    private String modelPath;

    @Override
    public String getParentPath() {
        return parentPath;
    }

    @Override
    public String getModelPath() {
        return modelPath;
    }

    @Override
    public Boolean isEmpty() {
        return parentPath == null || parentPath.isEmpty() || modelPath == null || modelPath.isEmpty();
    }
}
