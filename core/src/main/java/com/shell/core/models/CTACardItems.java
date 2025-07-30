
package com.shell.core.models;

/**
 * Interface representing a single item in a CTA (Call-To-Action) Cards component.
 * <p>
 * Each item typically includes an icon and a title, and may optionally include
 * other elements such as descriptions or buttons depending on the implementation.
 *
 * @author Saraswathi
 * @version 1.0
 * @since 22-05-2025
 */
public interface CTACardItems {

    /**
     * Returns the path to the icon or image associated with the CTA card.
     *
     * @return String representing the image path (can be null if not provided)
     */
    public String getIconReference();

    /**
     * Returns the title or heading text for the CTA card.
     *
     * @return String representing the card title (should not be null if required)
     */
    public String getTitle();
}