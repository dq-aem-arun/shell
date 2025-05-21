package com.shell.core.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.shell.core.models.ShellButton;

@Model(adaptables = Resource.class,
adapters = ShellButton.class,
resourceType = ShellButton.RESOURCE_TYPE,
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShellButtonImpl implements ShellButton {


     @ChildResource(name = "buttons")
    private List<Resource> buttons;

    @Override
    public List<ShellButtonItem> getButtons() {
        if (buttons == null) {
            return Collections.emptyList();
        }

        List<ShellButtonItem> buttonItems = new ArrayList<>();
        for (Resource button : buttons) {
            buttonItems.add(new ShellButtonItemImpl(button));
        }
        return buttonItems;
    }
    @Override
    public boolean isEmpty() {
        return buttons == null || buttons.isEmpty();
    }
    
    public static class ShellButtonItemImpl implements ShellButtonItem {

        @ValueMapValue
        private final String buttonText;
        @ValueMapValue
        private final String buttonLink;
        @ValueMapValue
        private final String icon;

        public ShellButtonItemImpl(Resource resource) {
            this.buttonText = resource.getValueMap().get("buttonText", String.class);
            this.buttonLink = resource.getValueMap().get("buttonLink", String.class);
            this.icon = resource.getValueMap().get("icon", String.class);
        }

        @Override
        public String getButtonText() {
            return buttonText;
        }

        @Override
        public String getButtonLink() {
            return buttonLink;
        }

        @Override
        public String getIcon() {
            return icon;
        }
}
}