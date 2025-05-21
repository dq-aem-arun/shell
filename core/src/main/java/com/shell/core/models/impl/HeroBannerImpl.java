package com.shell.core.models.impl;

import com.shell.core.models.HeroBanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
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
@Slf4j
public class HeroBannerImpl implements HeroBanner {

    /**
     * The title text for the Hero Banner.
     */
    @ValueMapValue
    private String title;

    /**
     * The detailed description text for the Hero Banner.
     */
    @ValueMapValue
    private String details;

    /**
     * The image path or URL used in the Hero Banner.
     */
    @ValueMapValue
    private String image;

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
    public String getDetails() {
        log.info("Getting Details of HeroBanner component");
        return details;
    }

    /**
     * Returns the image path or URL associated with the Hero Banner.
     *
     * @return the image source
     */
    @Override
    public String getImage() {
        log.info("Getting Image of HeroBanner component");
        return image;
    }
}