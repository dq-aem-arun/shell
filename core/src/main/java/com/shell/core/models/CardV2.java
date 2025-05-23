package com.shell.core.models;

/**
 * Interface representing the CardV2 component model.
 * Provides access to card properties such as the title and image.
 */
public interface CardV2 {

    /**
     * Returns the title text for the card.
     *
     * @return the card title as a String
     */
    String getTitle();

    /**
     * Returns the image path or reference for the card.
     *
     * @return the image path as a String
     */
    String getImageReference();
}
