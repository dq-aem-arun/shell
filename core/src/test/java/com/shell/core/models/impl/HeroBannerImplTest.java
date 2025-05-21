package com.shell.core.models.impl;

import com.shell.core.models.HeroBanner;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link HeroBannerImpl} class using AEM Mocks.
 * <p>
 * This test class ensures that the Sling Model correctly maps and returns
 * expected values from the JSON content resource.
 * </p>
 */
@Slf4j
@ExtendWith(AemContextExtension.class)
class HeroBannerImplTest {

    /**
     * AEM context for simulating the content repository and Sling models.
     */
    AemContext context = new AemContext();

    /**
     * Sets up the test environment by loading the JSON content
     * and registering the model classes.
     */
    @BeforeEach
    void setUp() {
        log.info("Loading all required Resources in setUp() method of HeroBannerImplTest");
        context.addModelsForClasses(HeroBanner.class);
        context.load().json("/heroBanner/herobanner.json", "/component");
    }

    /**
     * Tests the {@link HeroBanner#getTitle()} method to ensure the title is
     * correctly adapted from the resource.
     */
    @Test
    void getTitleTest() {
        log.info("Testing title of HeroBanner component");
        context.currentResource("/component/container/shell_herobanner");
        HeroBanner heroBanner = context.currentResource().adaptTo(HeroBanner.class);
        assertNotNull(heroBanner, "HeroBanner model should not be null");

        String expected = "Every journey has a plus with Shell Go+";
        String actual = heroBanner.getTitle();
        assertEquals(expected, actual, "Title value did not match expected result");
    }

    /**
     * Tests the {@link HeroBanner#getDetails()} method to ensure the details
     * text is correctly adapted from the resource.
     */
    @Test
    void getDetailsTest() {
        log.info("Testing Details of HeroBanner component");
        context.currentResource("/component/container/shell_herobanner");
        HeroBanner heroBanner = context.currentResource().adaptTo(HeroBannerImpl.class);
        assertNotNull(heroBanner, "HeroBannerImpl model should not be null");

        String expected = "Make your journeys on and off the road more rewarding with the Shell Asia app.";
        String actual = heroBanner.getDetails();
        assertEquals(expected, actual, "Details value did not match expected result");
    }

    /**
     * Tests the {@link HeroBanner#getImage()} method to ensure the image path
     * is correctly adapted from the resource.
     */
    @Test
    void getImageTest() {
        log.info("Testing Image of HeroBanner component");
        context.currentResource("/component/container/shell_herobanner");
        HeroBannerImpl heroBanner = context.currentResource().adaptTo(HeroBannerImpl.class);
        assertNotNull(heroBanner, "HeroBannerImpl model should not be null");

        String expected = "/content/dam/shell/shell-go-plus-india-banner.jpeg";
        String actual = heroBanner.getImage();

        assertEquals(expected, actual, "Image path did not match expected result");
        assertNotEquals("/content/dam/shell/shell-go-plus-india-banner.jpe", actual, "Negative image test failed");
        assertSame(expected.intern(), actual.intern(), "Interned image strings do not match");
    }
}
