package com.shell.core.models.impl;

import com.shell.core.models.HeroBanner;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mohammad Shoaib
 * @version v1.0
 * @since 20-05-2025
 * <p>
 * Implementation class for the HeroBanner Sling Model.
 * <p>
 * This model is adapted from a Sling Resource and is used to expose
 * content properties for the HeroBanner AEM component.
 * </p>
 *
 * <p>
 * The fields such as title, details, and image are injected from the
 * resource's value map and made available via getter methods.
 * </p>
 */
@Model(
        adaptables = Resource.class,
        adapters = HeroBanner.class,
        resourceType = HeroBanner.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroBannerImpl implements HeroBanner {
    private static final Logger log = LoggerFactory.getLogger(HeroBannerImpl.class);

    /**
     * The title text for the Hero Banner.
     */
    @ValueMapValue
    private String title;

    /**
     * The detailed description text for the Hero Banner.
     */
    @ValueMapValue
    private String description;

    /**
     * The image path or URL used in the Hero Banner.
     */
    @ValueMapValue
    private String fileReference;

    /**
     * Returns the title of the Hero Banner component.
     *
     * @return the title text
     */
    @Override
    public String getTitle() {
        log.info("Getting Title of HeroBanner component");
        return title;
    }

    /**
     * Returns the details/description of the Hero Banner component.
     *
     * @return the detailed text
     */
    @Override
    public String getDescription() {
        log.info("Getting Details of HeroBanner component");
        return description;
    }

    /**
     * Returns the image path or URL associated with the Hero Banner.
     *
     * @return the image source
     */
    @Override
    public String getFileReference() {
        log.info("Getting Image of HeroBanner component");
        return fileReference;
    }
}