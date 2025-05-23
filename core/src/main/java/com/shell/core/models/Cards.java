package com.shell.core.models;

import java.util.List;

/**
 * Interface for the Cards model.
 * Represents a container that holds multiple card items.
 */
public interface Cards {

    /**
     * @return list of card items.
     */
    List<CardsItems> getCards();

    /**
     * Checks if the component is empty (i.e., no cards are configured).
     *
     * @return true if the component has no cards, false otherwise.
     */
    public boolean isEmpty();

}