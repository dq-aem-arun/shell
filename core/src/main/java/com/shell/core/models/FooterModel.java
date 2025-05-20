package com.shell.core.models;

import java.util.List;

public interface FooterModel{

    List<FooterNavGroup> getFooterSiteNavigation();

    List<FooterNavItem> getFooterSocialNavigation();

    List<FooterNavItem> getFooterLegalLinks();

    String getSocialNavigationlinksTitle();

    interface FooterNavGroup {
        String getGroupName();
        List<FooterNavItem> getItems();
    }

    interface FooterNavItem {
        String getLabel();
        String getPath();
        String getIconPath(); // for social only; null otherwise
    }
}
