package com.shell.core.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shell.core.models.ShellButton;
import com.shell.core.models.ShellButtonItem;

/**
 * Implementation of the ShellButton interface.
 * This class provides the logic for retrieving button data and checking if the
 * component is empty.
 * 
 * @author ManojKumar
 * @version 1.0
 * @since 19-05-2025
 */
@Model(adaptables = Resource.class, adapters = ShellButton.class, resourceType = ShellButton.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShellButtonImpl implements ShellButton {

    // Logger instance for debugging
    private static final Logger LOG = LoggerFactory.getLogger(ShellButtonImpl.class);

    // Child resource containing the list of button items
    @ChildResource(name = "buttons")
    private List<Resource> buttons;

    /**
     * Retrieves the list of button items configured for the component.
     *
     * @return a list of ShellButtonItem objects representing the buttons, or an
     *         empty list if no buttons are configured.
     */
    @Override
    public List<ShellButtonItem> getButtons() {
        if (buttons == null) {
            LOG.info("No buttons found under the 'buttons' child resource. Returning an empty list.");
            return Collections.emptyList();
        }

        LOG.info("Found {} button resources. Processing them...", buttons.size());
        List<ShellButtonItem> buttonItems = new ArrayList<>();
        for (Resource button : buttons) {
            buttonItems.add(new ShellButtonItemImpl(button));
        }
        LOG.info("Processed all button resources. Returning the list of button items.");
        return buttonItems;
    }

    /**
     * Checks if the component is empty (i.e., no buttons are configured).
     *
     * @return true if the component has no buttons, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        boolean isEmpty = buttons == null || buttons.isEmpty();
        LOG.info("Checking if the component is empty: {}", isEmpty);
        return isEmpty;
    }
}