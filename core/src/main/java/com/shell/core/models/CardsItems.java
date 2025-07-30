package com.shell.core.models;

/**
 * Interface for a single Card item.
 * Represents individual card properties like image, title, description, and button visibility.
 */
public interface CardsItems {

    /**
     * @return image path for the card
     */
    public String getFileReference();

    /**
     * @return title text of the card.
     */
    public String getTitle();

    /**
     * @return description text of the card.
     */
    public String getDescription();

    /**
     * @return true if the button should be shown, false otherwise.
     */
    public boolean getShowButton();

    /**
     * @return true if description is null or empty, false otherwise.
     */
    public boolean isDesscriptionEmpty();
}