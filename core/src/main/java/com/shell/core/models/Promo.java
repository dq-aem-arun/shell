package com.shell.core.models;

import java.util.List;
/**
 * Interface for the Promo model.
 * Represents a container that holds a list of promotional banner components.
 */
public interface Promo {

    /**
     * Gets the list of promotional banners.
     *
     * @return list of {@link Promobanner} items.
     */
    List<Promobanner> getPromoBanner();
    /**
     * Checks if the Promo model is empty.
     *
     * Implementations should return true if there are no promotional banners
     * or if the banner list is empty, indicating no content to render.
     *
     * @return true if the model is empty, false otherwise.
     */
    public boolean isEmpty();
}
