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
}
