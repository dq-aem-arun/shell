/**
 * Represents the contract for Footer component model used in the website.
 * Provides access to different navigation groups such as site links,
 * social navigation, and legal links.
 *
 * @author Jay
 * @version v1.0.0
 * @since 19-05-2025
 */
package com.shell.core.models;

import java.util.List;

/**
 * Footer interface defines the structure for footer navigation sections.
 */
public interface Footer {

    /**
     * Returns the list of site navigation groups in the footer.
     *
     * @return List of FooterNavGroup representing site navigation sections.
     */
    List<FooterNavGroup> getFooterSiteNavigation();

    /**
     * Returns the list of social navigation items in the footer.
     *
     * @return List of FooterNavItem representing social links.
     */
    List<FooterNavItem> getFooterSocialNavigation();

    /**
     * Returns the list of legal links in the footer.
     *
     * @return List of FooterNavItem representing legal links.
     */
    List<FooterNavItem> getFooterLegalLinks();

    /**
     * Returns the title for the social navigation links section.
     *
     * @return String representing the social navigation section title.
     */
    String getSocialNavigationlinksTitle();

    /**
     * Checks if all footer sections (site navigation, social, legal) are empty or
     * null.
     *
     * @return true if all are empty or null
     */
    boolean isEmpty();

    /**
     * Represents a group of navigation items in the footer.
     */
    interface FooterNavGroup {
        /**
         * Returns the name of the navigation group.
         *
         * @return String representing the group name.
         */
        String getGroupName();

        /**
         * Returns the list of navigation items in this group.
         *
         * @return List of FooterNavItem in the group.
         */
        List<FooterNavItem> getItems();
    }
}
}