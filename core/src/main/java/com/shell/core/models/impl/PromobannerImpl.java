package com.shell.core.models.impl;

import com.shell.core.models.Promobanner;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link Promobanner} interface.
 * Represents a single promotional banner with an image, title, and description.
 */
@Model(adaptables = Resource.class, adapters = Promobanner.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PromobannerImpl implements Promobanner {

    private static final Logger LOG = LoggerFactory.getLogger(PromobannerImpl.class);

    /**
     * Image reference path for the banner.
     */
    @ValueMapValue
    private String fileReference;

    /**
     * Title text for the banner.
     */
    @ValueMapValue
    private String title;

    /**
     * Description text for the banner.
     */
    @ValueMapValue
    private String description;

    /**
     * Enable the button for the banner.
     *
     * @return the boolean value as a boolean.
     */
    @ValueMapValue
    private boolean showButton;

    /**
     * Gets the image file reference for the banner.
     *
     * @return the file reference as a String.
     */
    @Override
    public String getFileReference() {
        LOG.debug("Getting fileReference: {}", fileReference);
        return fileReference;
    }

    /**
     * Gets the title of the banner.
     *
     * @return the title as a String.
     */
    @Override
    public String getTitle() {
        LOG.debug("Getting title: {}", title);
        return title;
    }

    /**
     * Gets the description of the banner.
     *
     * @return the description as a String.
     */
    @Override
    public String getDescription() {
        LOG.debug("Getting description: {}", description);
        return description;
    }

    /**
     * Enable the button for the banner.
     *
     * @return the boolean value as a boolean.
     */
    @Override
    public boolean getShowButton() {
        LOG.debug("Getting showButton: {}", showButton);
        return showButton;
    }
}
