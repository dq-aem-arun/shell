package com.shell.core.models.impl;

import com.shell.core.models.CTACardItems;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model implementation for individual CTA (Call-To-Action) card items.
 * This model is adaptable from a Resource and provides properties like icon reference and title.
 *
 * @author Saraswathi
 * @version 1.0
 * @since 22-05-2025
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, adapters = CTACardItems.class)
public class CTACardItemsImpl implements CTACardItems {

    // SLF4J Logger instance
    private static final Logger LOG = LoggerFactory.getLogger(CTACardItemsImpl.class);

    // Injects the value of the 'iconReference' property from the resource's value map
    @ValueMapValue
    private String iconReference;

    // Injects the value of the 'title' property from the resource's value map
    @ValueMapValue
    private String title;

    /**
     * Returns the icon reference path for the CTA card.
     *
     * @return iconReference as String
     */
    @Override
    public String getIconReference() {
        LOG.info("Retrieving icon reference: {}", iconReference);
        return iconReference;
    }

    /**
     * Returns the title of the CTA card.
     *
     * @return title as String
     */
    @Override
    public String getTitle() {
        LOG.info("Retrieving title: {}", title);
        return title;
    }
}
