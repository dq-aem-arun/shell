package com.shell.core.models.impl;

import com.shell.core.models.CTACard;
import com.shell.core.models.CTACardItems;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    /**
     * Injects a list of child resources under the current resource.
     * These resources must be adaptable to the CTACardItems interface.
     */
    @ChildResource
    private List<CTACardItems>ctaCards;

    /**
     * Returns the list of CTA card items configured in the dialog's multifield.
     *
     * @return List of CTACardItems
     */
    @Override
    public List<CTACardItems> getCtaCards() {
        LOG.info("Fetching CTA cards list. Size: {}",
                ctaCards != null ? ctaCards.size() : "null");
        return ctaCards;
    }

    @Override
    public boolean isEmpty() {
        boolean isEmpty = ctaCards == null || ctaCards.isEmpty();
        LOG.info("Checking if the component is empty: {}", isEmpty);
        return isEmpty;
    }
}
