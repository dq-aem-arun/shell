package com.shell.core.models;

import java.util.List;

public interface ShellButton {
    

    String RESOURCE_TYPE = "shell/components/content/button";
 List<ShellButtonItem> getButtons();
   boolean isEmpty();
    interface ShellButtonItem {
        String getButtonText();
        String getButtonLink();
        String getIcon();
    }
}
