package com.shell.core.models.impl;

import com.shell.core.models.PlayStroreCard;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Bhagyalaxmi
 * @version : v1.0
 * @since : 20-05-2025
 * <p>
 * Sling Model implementation for the PlayStoreCard component.
 * This model is responsible for exposing authored dialog values such as the
 * title, description, app store image references (Google Play and App Store),
 * and a call-to-action URL. These fields are typically used to promote mobile app downloads.
 */

@Model(adaptables = Resource.class, adapters = PlayStroreCard.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlayStroreCardImpl implements PlayStroreCard {

    private static final Logger log = LoggerFactory.getLogger(PlayStroreCardImpl.class);

    @ValueMapValue
    private String titlefield;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String firstImageReference;

    @ValueMapValue
    private String firstImageLinkURL;

    @ValueMapValue
    private String secondImageReference;

    @ValueMapValue
    private String secondImageLinkURL;

    /**
     * Returns the title displayed in the PlayStoreCard component.
     * Typically used as the main heading above the download badges.
     *
     * @return the card title text
     */
    @Override
    public String getTitlefield() {
        log.info("getTitlefield() called");
        return titlefield;
    }

    /**
     * Returns the descriptive text content displayed in the component.
     * Often used to describe app features or encourage users to download the app.
     *
     * @return the descriptive content as a string
     */
    @Override
    public String getDescription() {
        log.info("getDescription() called");
        return description;
    }

    /**
     * Returns the path to the first app badge image (usually the Google Play badge).
     * This image is typically stored in the DAM and rendered in the component.
     *
     * @return the image reference path for the Google Play badge
     */
    public String getFirstImageReference() {
        log.info("getFirstImageReference() called");
        return firstImageReference;
    }

    /**
     * Returns the link URL that users are redirected to when clicking on the badges.
     * Usually this is the app store URL for downloading the mobile application.
     *
     * @return the external or internal link URL
     */
    @Override
    public String getFirstImageLinkURL() {
        log.info("getFirstImageLinkURL() called");
        return firstImageLinkURL;
    }

    /**
     * Returns the link URL that users are redirected to when clicking on the Google Play badge.
     * Typically, this directs to the Shell Asia App page on the Google Play Store,
     * allowing users to download the app and start collecting rewards.
     *
     * @return the URL for the Google Play Store badge
     */
    public String getSecondImageReference() {
        log.info("getSecondImageReference() called");
        return secondImageReference;
    }

    /**
     * Returns the link URL that users are redirected to when clicking on the App Store badge.
     * Typically, this directs to the Shell Asia App page on the Apple App Store,
     * allowing users to download the app and start collecting rewards.
     *
     * @return the URL for the Apple App Store badge
     */
    @Override
    public String getSecondImageLinkURL() {
        log.info("getSecondImageLinkURL() called");
        return secondImageLinkURL;
    }
}
