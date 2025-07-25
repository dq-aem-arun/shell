package com.shell.core.models;
/**
 * Interface for a single promotional banner component.
 * Represents an individual banner with an image, title, and description.
 */
public interface Promobanner {
    /**
     * Gets the image file reference for the banner.
     *
     * @return the file reference as a String.
     */
    String getFileReference();
    /**
     * Gets the title text of the banner.
     *
     * @return the title as a String.
     */
    String getTitle();
    /**
     * Gets the description text of the banner.
     *
     * @return the description as a String.
     */
    String getDescription();
    boolean getShowButton();
}