package com.shell.core.models.impl;

import com.shell.core.models.CTACard;
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
@Model(adaptables = Resource.class,adapters =CTACard.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CTACardsImpl implements CTACard {
    // SLF4J Logger instance
    private static final Logger LOG = LoggerFactory.getLogger(CTACardsImpl.class);

    @ValueMapValue
    private String iconReference;
    @ValueMapValue
    private String title;

    @Override
    public boolean isEmpty() {
        boolean isEmpty = iconReference == null || title == null;
        LOG.info("Checking if the component is empty: {}", isEmpty);
        return isEmpty;
    }

    @Override
    public String getIconReference() {
        LOG.info("Retrieving icon reference: {}", iconReference);
        return iconReference;
    }


    @Override
    public String getTitle() {
        LOG.info("Retrieving icon reference: {}", iconReference);
        return title;
    }


}
