package com.shell.core.models;

import java.util.List;

/**
 * Interface representing the contract for a CTA (Call-To-Action) Cards component.
 * <p>
 * This interface provides a method to retrieve a list of CTA card items,
 * which are typically displayed in a grid or list layout on the front end.
 * <p>
 * Each CTA card item contains content like image, title, and optional action elements.
 *
 * @author Saraswathi
 * @version 1.0
 * @since 22-05-2025
 */
public interface CTACard {

    /**
     * Returns the list of CTA card items configured by the author.
     *
     * @return List of CTACardItems representing individual cards
     */
    List<CTACardItems> getCtaCards();
}
