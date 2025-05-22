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
}
