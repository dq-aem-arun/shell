package com.shell.core.models.impl;

import com.shell.core.models.CardV2;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Bhagyalaxmi
 * @version : v1.0
 * @since : 23-05-2025
 * <p>
 * Sling Model implementation for the CardV2 component.
 * Exposes title and image reference fields from the dialog for rendering cards in views.
 * Logs each method call for debugging purposes.
 */
@Model(adaptables = Resource.class, adapters = CardV2.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardV2Impl implements CardV2 {

    private static final Logger log = LoggerFactory.getLogger(CardV2Impl.class);

    @ValueMapValue
    private String title;

    @ValueMapValue(name = "imageReference")
    private String imageReference;

    @ValueMapValue
    private boolean showButton;

    /**
     * Returns the title for the card component.
     * Logged for debug tracing during Sling model usage.
     *
     * @return the card title
     */
    @Override
    public String getTitle() {
        log.info("getTitle() called - returning: {}", title);
        return title;
    }

    /**
     * Returns the image path for the card component.
     * Logged for debug tracing during Sling model usage.
     *
     * @return the image reference path
     */
    @Override
    public String getImageReference() {
        log.info("getImageReference() called - returning: {}", imageReference);
        return imageReference;
    }

    /**
     * Returns the boolean flag indicating whether a button should be displayed on the card.
     * Logged for debug tracing during Sling model usage.
     *
     * @return true if the button should be shown; false otherwise
     */
    @Override
    public boolean getShowButton() {
        log.info("getShowButton() called - returning: {}", showButton);
        return showButton;
    }

    /**
     * Evaluates whether the CardV2 component contains meaningful content.
     * A card is considered empty if:
     * - the title is null or empty,
     * - the image reference is null or empty,
     * - and the showButton flag is false.
     * This helps in preventing the rendering of incomplete or placeholder cards in the UI.
     *
     * @return true if all key fields are empty or false; false otherwise
     */
    @Override
    public boolean isEmpty() {
        boolean isEmpty = (title == null || title.isEmpty()) && (imageReference == null || imageReference.isEmpty()) && !showButton;
        log.info("Checking if the components is empty: {}", isEmpty);
        return isEmpty;
    }
}
