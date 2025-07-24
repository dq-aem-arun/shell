package com.shell.core.models;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a navigation item in the footer, such as a link or social icon.
 */
public class FooterNavItem {
    // Logger for logging events and debugging
    private static final Logger LOG = LoggerFactory.getLogger(FooterNavItem.class);

    // The display label for the navigation item
    private final String label;

    // The path or URL the navigation item points to
    private final String path;

    // The path to the icon for the navigation item (optional)
    private final String iconPath;

    /**
     * Constructs a FooterNavItem from a Sling Resource.
     *
     * @param resource    The resource containing navigation item properties.
     * @param includeIcon Whether to include the icon path.
     */
    public FooterNavItem(Resource resource, boolean includeIcon) {
        this.label = resource.getValueMap().get("label", "");
        this.path = resource.getValueMap().get("path", "");
        this.iconPath = includeIcon ? resource.getValueMap().get("iconpath", "") : null;

        // Logging the creation of the FooterNavItem
        LOG.debug("FooterNavItem created: label='{}', path='{}', iconPath='{}'", label, path, iconPath);
    }

    /**
     * Gets the display label for the navigation item.
     *
     * @return the label string
     */
    public String getLabel() {
        LOG.debug("getLabel() called, returning label='{}'", label);
        return label;
    }

    /**
     * Gets the path or URL for the navigation item.
     *
     * @return the path string
     */
    public String getPath() {
        LOG.debug("getPath() called, returning path='{}'", path);
        return path;
    }

    /**
     * Gets the icon path for the navigation item, if available.
     *
     * @return the icon path string or null if not included
     */
    public String getIconPath() {
        LOG.debug("getIconPath() called, returning iconPath='{}'", iconPath);
        return iconPath;
    }
}