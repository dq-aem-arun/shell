package com.shell.core.models;

import java.util.List;

/**
 * Interface representing the Shell Button component.
 * This interface defines the contract for retrieving button data
 * and checking if the component is empty.
 */
public interface ShellButton {

    /**
     * The resource type associated with the Shell Button component.
     * This should match the resource type defined in the component's dialog.
     */
    String RESOURCE_TYPE = "shell/components/content/shell-button";

    /**
     * Retrieves the list of buttons configured for the component.
     *
     * @return a list of {@link ShellButtonItem} objects representing the buttons.
     */
    List<ShellButtonItem> getButtons();

    /**
     * Checks if the component is empty (i.e., no buttons are configured).
     *
     * @return true if the component has no buttons, false otherwise.
     */
    boolean isEmpty();

}
