package com.shell.core.models;

/**
 * Interface representing the PlayStoreCard component model.
 * Provides access to configured properties such as the title, description,
 * image references for Google Play and App Store badges, and their respective link URLs.
 */
public interface PlayStroreCard {

    /**
     * Returns the title text to be displayed on the card.
     *
     * @return the title as a {@link String}
     */
    String getTitlefield();

    /**
     * Returns the description text to be displayed on the card.
     *
     * @return the description as a {@link String}
     */
    String getDescription();

    /**
     * Returns the image path or reference for the App Store badge.
     *
     * @return the App Store image path as a {@link String}
     */
    String getFirstImageReference();

    /**
     * Returns the hyperlink URL for the Google Play badge.
     *
     * @return the Google Play badge URL as a {@link String}
     */
    String getFirstImageLinkURL();

    /**
     * Returns the image path or reference for the App Store badge.
     *
     * @return the App Store image path as a {@link String}
     */
    String getSecondImageReference();

    /**
     * Returns the hyperlink URL for the App Store badge.
     *
     * @return the App Store badge URL as a {@link String}
     */
    String getSecondImageLinkURL();
}
