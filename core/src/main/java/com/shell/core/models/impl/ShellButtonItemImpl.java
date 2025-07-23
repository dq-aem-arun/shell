package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shell.core.models.ShellButtonItem;

/**
 * Implementation of the ShellButtonItem interface.
 * Represents a single button item with properties such as text, link, and icon.
 * 
 * @author ManojKumar
 * @version 1.0
 * @since 19-05-2025
 */
public class ShellButtonItemImpl implements ShellButtonItem {

    // Logger instance for debugging
    private static final Logger LOG = LoggerFactory.getLogger(ShellButtonItemImpl.class);

    // Button text (e.g., "Read More")
    @ValueMapValue
    private final String buttonText;

    // Button link (e.g., "/content/page.html")
    @ValueMapValue
    private final String buttonLink;

    // Icon path (e.g., "/content/dam/icon.png")
    @ValueMapValue
    private final String icon;

    /**
     * Constructor to initialize the ShellButtonItemImpl with a resource.
     * Retrieves the button properties from the resource's ValueMap.
     *
     * @param resource the resource representing the button item
     */
    public ShellButtonItemImpl(Resource resource) {
        LOG.info("Initializing ShellButtonItemImpl for resource: {}", resource.getPath());
        this.buttonText = resource.getValueMap().get("buttonText", String.class);
        this.buttonLink = resource.getValueMap().get("buttonLink", String.class);
        this.icon = resource.getValueMap().get("icon", String.class);

        // Log the retrieved values for debugging
        LOG.info("Button Text: {}", buttonText);
        LOG.info("Button Link: {}", buttonLink);
        LOG.info("Icon: {}", icon);
    }

    /**
     * @return the text of the button
     */
    @Override
    public String getButtonText() {
        LOG.info("Returning button text: {}", buttonText);
        return buttonText;
    }

    /**
     * @return the link of the button
     */
    @Override
    public String getButtonLink() {
        LOG.info("Returning button link: {}", buttonLink);
        return buttonLink;
    }

    /**
     * @return the path to the icon
     */
    @Override
    public String getIcon() {
        LOG.info("Returning icon path: {}", icon);
        return icon;
    }
}
