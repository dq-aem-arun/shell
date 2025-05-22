package com.shell.core.models.impl;

import com.shell.core.models.Promobanner;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link PromoImpl} using AEM mock context.
 */
@ExtendWith(AemContextExtension.class)
class PromoImplTest {

    private final AemContext context = new AemContext();

    /**
     * Sets up the AEM context with models and test JSON content.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(PromoImpl.class, PromobannerImpl.class);
        context.load().json("/jsonfiles/PromoImplTest.json", "/content");
    }

    /**
     * Tests that the {@link PromoImpl#getPromoBanner()} method correctly adapts and returns
     * a list of {@link Promobanner} objects with expected values.
     */
    @Test
    void getPromoBanner() {
        context.currentResource("/content");
        PromoImpl promoImpl = context.currentResource().adaptTo(PromoImpl.class);

        List<Promobanner> promobanner = promoImpl.getPromoBanner();

        assertNotNull(promobanner, "Promo banner list should not be null");
        assertEquals(2, promobanner.size(), "Expected 2 promo banners");

        // Verify first item
        Promobanner firstItem = promobanner.get(0);
        assertEquals("/content/dam/sample1.jpg", firstItem.getFileReference());
        assertEquals("Unleash the power within", firstItem.getTitle());
        assertEquals("Shell Helix Ultra with Pure Plus technology keeps your engine strong, because only by unlocking the true power within can we push ourselves to the next level.", firstItem.getDescription());

        // Verify second item
        Promobanner secondItem = promobanner.get(1);
        assertEquals("/content/dam/sample2.jpg", secondItem.getFileReference());
        assertEquals("Offers and Promotions", secondItem.getTitle());
        assertEquals("Discover exciting offers on wide range of products. Don’t let this deals slip away-redeem now!", secondItem.getDescription());
    }
}
