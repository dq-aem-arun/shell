/*
 * Author : Jay
 * Created Date:19-05-2025
 * Version: v1.0.0
 */
package com.shell.core.models.impl;

import com.shell.core.models.Footer;
import com.shell.core.models.FooterNavItem;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(FooterImpl.class);

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
        LOG.debug("Initializing FooterImpl...");
        if (footerLegalLinksContainer != null) {
            LOG.debug("Found legal links container: {}", footerLegalLinksContainer.getPath());
            for (Resource item : footerLegalLinksContainer.getChildren()) {
                legalNavItems.add(new FooterNavItem(item, false));
                LOG.debug("Added legal footer item: {}", item.getPath());
            }
        } else {
            LOG.warn("Legal links container not found.");
        }
    }

    @Override
    public List<FooterNavGroup> getFooterSiteNavigation() {
        LOG.debug("Fetching footer site navigation groups...");
        List<FooterNavGroup> groups = new ArrayList<>();
        if (footerGroups != null) {
            for (Resource group : footerGroups) {
                LOG.debug("Processing footer group: {}", group.getPath());
                groups.add(new FooterNavGroupImpl(group));
            }
        } else {
            LOG.warn("No footerGroups found.");
        }
        return groups;
    }

    @Override
    public List<FooterNavItem> getFooterSocialNavigation() {
        LOG.debug("Fetching footer social navigation items...");
        return buildNavItemList(socialNavItems, true);
    }

    @Override
    public List<FooterNavItem> getFooterLegalLinks() {
        LOG.debug("Returning legal navigation items count: {}", legalNavItems.size());
        return legalNavItems;
    }

    private List<FooterNavItem> buildNavItemList(List<Resource> resources, boolean includeIcon) {
        List<FooterNavItem> items = new ArrayList<>();
        if (resources != null) {
            for (Resource item : resources) {
                items.add(new FooterNavItem(item, includeIcon));
                LOG.debug("Built FooterNavItem from resource: {}, includeIcon={}", item.getPath(), includeIcon);
            }
        } else {
            LOG.warn("No resources found for building nav items.");
        }
        return items;
    }

    @Override
    public String getSocialNavigationlinksTitle() {
        LOG.debug("Returning social navigation title: {}", socialNavigationlinksTitle);
        return socialNavigationlinksTitle;
    }

    // --- Inner classes ---
    private static class FooterNavGroupImpl implements FooterNavGroup {
        private final String groupName;
        private final List<FooterNavItem> items;

        private static final Logger LOG = LoggerFactory.getLogger(FooterNavGroupImpl.class);

        FooterNavGroupImpl(Resource resource) {
            this.groupName = resource.getValueMap().get("group", "");
            LOG.debug("Initializing FooterNavGroupImpl with group name: {}", groupName);

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
                LOG.debug("Added FooterNavItem to group '{}': {}", groupName, res.getPath());
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
