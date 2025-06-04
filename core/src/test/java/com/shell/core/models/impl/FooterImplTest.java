package com.shell.core.models.impl;

import com.shell.core.models.Footer;
import com.shell.core.models.FooterNavItem;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
/*
 * @author Jay
 * @version v1.0.0
 * @since 19-05-2025
 */
@ExtendWith(AemContextExtension.class)
public class FooterImplTest {

    AemContext context = new AemContext();
    Footer footerModel;

    @BeforeEach
    void setUp() {
        context.load().json("/footer/footer.json", "/content");
        context.addModelsForClasses(FooterImpl.class);

    }

    @Test
    void testFooterSiteNavigation() { // Scenario to check footerSiteNavigation ie group and group names
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        assertNotNull(footerModel);
        List<Footer.FooterNavGroup> footerSiteNavigation = footerModel.getFooterSiteNavigation();
        assertNotNull(footerSiteNavigation);
        String[] groupNamesActual = new String[footerSiteNavigation.size()];
        for (int i = 0; i < groupNamesActual.length; i++) {
            groupNamesActual[i] = footerSiteNavigation.get(i).getGroupName();
        }
        String[] groupNamesExpected = new String[footerSiteNavigation.size()];
        groupNamesExpected[0] = "Group A";
        groupNamesExpected[1] = "Group B";
        // test For Group Name Outer multifield
        assertArrayEquals(groupNamesExpected, groupNamesActual);
    }

    // test for inner multifield NavItems
    @Test
    void testGetItems() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        List<FooterNavItem> navItems = footerModel.getFooterSiteNavigation().get(0).getItems();
        assertNotNull(navItems);
        assertEquals("Home", navItems.get(0).getLabel());
        assertEquals("/home", navItems.get(0).getPath(), "expected result not matched");
    }

    // test For Social Navigations multifield
    @Test
    void testGetFooterSocialNavigation() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        List<FooterNavItem> footerSocialNavigation = footerModel.getFooterSocialNavigation();
        assertNotNull(footerSocialNavigation);
        FooterNavItem footerNavItem = footerSocialNavigation.get(0);
        assertNotNull(footerNavItem);
        assertEquals("Facebook", footerNavItem.getLabel());
        assertEquals("/facebook", footerNavItem.getPath());
        assertEquals("/icons/facebook.svg", footerNavItem.getIconPath());
    }

    // Test For Footer Legal Links multifield Bottom part of footer
    @Test
    void testGetFooterLegalLinks() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        assertNotNull(footerModel);
        List<FooterNavItem> footerLegalLinks = footerModel.getFooterLegalLinks();
        assertNotNull(footerLegalLinks);
        FooterNavItem footerNavItem = footerLegalLinks.get(0);
        assertEquals("Privacy Policy", footerNavItem.getLabel());
        assertEquals("/legal/privacy", footerNavItem.getPath());
        assertNull(footerNavItem.getIconPath());
    }

    // Test for Social Navigation Title
    @Test
    void testGetSocialNavigationLinksTitle() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        String socialNavigationlinksTitle = footerModel.getSocialNavigationlinksTitle();
        assertNotNull(socialNavigationlinksTitle);
        assertEquals("Contact Us", socialNavigationlinksTitle);
    }
    // Test for Footer Legal Links Size
    @Test
    void testCountOfFooterSiteNavigation() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        int size = footerModel.getFooterSiteNavigation().size();
        assertEquals(2, size);

    }
    // Test for Footer Social Navigation
    @Test
    void testCountOfFooterSocialNavigation() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        int size = footerModel.getFooterSocialNavigation().size();
        assertEquals(1, size);

    }
    // Test for Footer Legal Links multifield Bottom part of footer
    @Test
    void testCountOfFooterLegalLinks() {
        context.currentResource("/content/footer-2");
        footerModel = context.currentResource().adaptTo(Footer.class);
        int size = footerModel.getFooterLegalLinks().size();
        assertEquals(2, size);

    }
    //test for isFooterEmpty method
    @Test
    void testFooterSiteNavigationWhenEmpty() {
        context.currentResource("/content/footer-3");
        footerModel = context.currentResource().adaptTo(Footer.class);
        List<Footer.FooterNavGroup> groups = footerModel.getFooterSiteNavigation();
        assertNotNull(groups);
        assertTrue(groups.isEmpty(), "Expected no footer nav groups");
    }
}
