package com.shell.core.models;

/**
 * Interface representing a single button item in the Shell Button component.
 * This interface defines the contract for retrieving button properties such as
 * text, link, and icon.
 * 
 * @author ManojKumar
 * @version 1.0
 * @since 19-05-2025
 */
public interface ShellButtonItem {

    /**
     * Retrieves the text of the button.
     * 
     * @return the button text (e.g., "Read More").
     */
    String getButtonText();

    /**
     * Retrieves the link associated with the button.
     * 
     * @return the button link (e.g., "/content/page.html").
     */
    String getButtonLink();

    /**
     * Retrieves the icon path for the button.
     * 
     * @return the path to the icon (e.g., "/content/dam/icon.png").
     */
    String getIcon();
}
