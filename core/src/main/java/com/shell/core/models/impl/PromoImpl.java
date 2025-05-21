package com.shell.core.models.impl;

import com.shell.core.models.Promobanner;
import com.shell.core.models.Promo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * @author N.Chandrika
 * @version 1.0
 * @since 2025-05-21
 * Implementation of the {@link Promo} interface.
 * Represents a container model that holds a list of promotional banners.
 */
@Model(adaptables = Resource.class, adapters = Promo.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PromoImpl implements Promo {

    private static final Logger LOG = LoggerFactory.getLogger(PromoImpl.class);

    /**
     * List of promotional banner child resources under the "promobanner" node.
     */
    @ChildResource(name = "promobanner")
    private List<Promobanner> promobanner;

    /**
     * Gets the list of promotional banner items.
     *
     * @return list of {@link Promobanner} instances.
     */
    @Override
    public List<Promobanner> getPromoBanner() {
        if (promobanner != null) {
            LOG.debug("Retrieved {} promo banners.", promobanner.size());
        } else {
            LOG.warn("No promo banners found.");
        }
        return promobanner;
    }
}
