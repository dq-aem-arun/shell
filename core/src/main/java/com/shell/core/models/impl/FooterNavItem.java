package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;

public class  FooterNavItem {
        private final String label;
        private final String path;
        private final String iconPath;

        FooterNavItem(Resource resource, boolean includeIcon) {
            this.label = resource.getValueMap().get("label", "");
            this.path = resource.getValueMap().get("path", "");
            this.iconPath = includeIcon ? resource.getValueMap().get("iconpath", "") : null;
        }

        public String getLabel() {
            return label;
        }

        public String getPath() {
            return path;
        }

        public String getIconPath() {
            return iconPath;
        }
    }