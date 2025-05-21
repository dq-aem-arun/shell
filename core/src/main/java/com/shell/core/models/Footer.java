/**
 * Author : Jay
 * Created Date:19-05-2025
 * Version: v1.0.0
 */
package com.shell.core.models;
import java.util.List;

public interface Footer{

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
