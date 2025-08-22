package com.shell.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationV3LogoModel {

    @ValueMapValue
    private String logo;

    @ValueMapValue
    private String logoAlt;

    @ValueMapValue
    private String logoPath;

    @ChildResource(name = "navIcons")
    private List<Resource> icons;

    public String getLogo() {
        return logo;
    }

    public String getLogoAlt() {
        return logoAlt;
    }

    public String getLogoPath() {
        return logoPath+".html";
    }

    public List<Icon> getIcons() {
        if (icons == null) {
            return Collections.emptyList();
        }

        return icons.stream()
                .filter(Objects::nonNull)
                .map(resource -> {
                    ValueMap props = resource.getValueMap();
                    return new Icon(
                            props.get("iconPath", String.class),
                            props.get("altText", String.class),
                            props.get("link", String.class)
                    );
                })
                .collect(Collectors.toList());
    }


    public static class Icon {
        private final String iconPath;
        private final String altText;
        private final String link;

        public Icon(String iconPath, String altText, String link) {
            this.iconPath = iconPath;
            this.altText = altText;
            this.link = link;
        }

        public String getIconPath() {
            return iconPath;
        }

        public String getAltText() {
            return altText;
        }

        public String getLink() {
            return link;
        }
    }
}