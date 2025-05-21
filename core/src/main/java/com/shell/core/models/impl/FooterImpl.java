
/*
 * Author : Jay
 * Created Date:19-05-2025
 * Version: v1.0.0
 */
package com.shell.core.models.impl;

import com.shell.core.models.Footer;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Model(
        adaptables = Resource.class,
        adapters = Footer.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterImpl implements Footer {

    @ChildResource(name = "groupNames")
    private List<Resource> footerGroups;

    @ChildResource(name = "footerItemsSocial")
    private List<Resource> socialNavItems;

    @ChildResource(name = "footerItemsLegalLinks")
    private Resource footerLegalLinksContainer;

    @ValueMapValue(name = "socialNavigationlinksTitle")
    private String socialNavigationlinksTitle;

    private List<FooterNavItem> legalNavItems = new ArrayList<>();

    @PostConstruct
    private void init() {
        if (footerLegalLinksContainer != null) {
            for (Resource item : footerLegalLinksContainer.getChildren()) {
                legalNavItems.add(new FooterNavItem(item, false));
            }
        }
    }

    @Override
    public List<FooterNavGroup> getFooterSiteNavigation() {
        List<FooterNavGroup> groups = new ArrayList<>();
        if (footerGroups != null) {
            for (Resource group : footerGroups) {
                groups.add(new FooterNavGroupImpl(group));
            }
        }
        return groups;
    }

    @Override
    public List<FooterNavItem> getFooterSocialNavigation() {
        return buildNavItemList(socialNavItems, true);
    }

    @Override
    public List<FooterNavItem> getFooterLegalLinks() {
        return legalNavItems;
    }

    private List<FooterNavItem> buildNavItemList(List<Resource> resources, boolean includeIcon) {
        List<FooterNavItem> items = new ArrayList<>();
        if (resources != null) {
            for (Resource item : resources) {
                items.add(new FooterNavItem(item, includeIcon));
            }
        }
        return items;
    }

    @Override
    public String getSocialNavigationlinksTitle() {
        return socialNavigationlinksTitle;
    }

    // --- Inner classes ---
    private static class FooterNavGroupImpl implements FooterNavGroup {
        private final String groupName;
        private final List<FooterNavItem> items;

        //constructor
        FooterNavGroupImpl(Resource resource) {
            this.groupName = resource.getValueMap().get("group", "");
            List<Resource> footerItems = Optional.ofNullable(resource.getChild("footerItems"))
                    .map(Resource::getChildren)
                    .map(iterable -> {
                        List<Resource> list = new ArrayList<>();
                        iterable.forEach(list::add);
                        return list;
                    })
                    .orElse(new ArrayList<>());
            this.items = new ArrayList<>();
            for (Resource res : footerItems) {
                items.add(new FooterNavItem(res, false));
            }
        }


        @Override
        public String getGroupName() {
            return groupName;
        }

        @Override
        public List<FooterNavItem> getItems() {
            return items; 
        } 
    }

    

  
}
