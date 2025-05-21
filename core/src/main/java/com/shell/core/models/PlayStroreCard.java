package com.shell.core.models;

/**
 * Interface representing the PlayStoreCard component model.
 * Provides access to configured properties such as title, description,
 * app store image references, and link URL.
 */
public interface PlayStroreCard {

    /**
     * Gets the title text to be displayed in the card.
     *
     * @return title as a String
     */
    String getTitlefield();

    /**
     * Gets the description text to be displayed in the card.
     *
     * @return description as a String
     */
    String getDescription();

    /**
     * Gets the image path for the Google Play badge.
     *
     * @return Google Play image path as a String
     */
    String getFirstImageReference();

    /**
     * Gets the image path for the App Store badge.
     *
     * @return App Store image path as a String
     */
    String getSecondImageReference();

    /**
     * Gets the hyperlink URL for the download action.
     *
     * @return link URL as a String
     */
    String getLinkURL();
}
