package com.shell.core.models.impl;

import com.shell.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for {@link PromoBanner_V3Impl}.
 * This class uses AEM Mocks to simulate AEM runtime environment
 * and verify that the PromoBanner model reads the expected values
 * from a JSON resource.
 * JSON path: /promobanner-v3/PromoBanner_V3ImplTest.json
 *
 * @author Saraswathi
 * @version 1.0
 * @since 26-05-2025
 */
@ExtendWith(AemContextExtension.class)
public class PromoBanner_V3ImplTest {

    // AEM context for simulating repository and resource resolution
    private final AemContext ctx = AppAemContext.newAemContext();

    /**
     * Sets up the test environment before each test method is executed.
     * Registers the model class and loads test JSON content.
     */
    @BeforeEach
    void setUp() {
        // Register the model to be used in AEM mock context
        ctx.addModelsForClasses(PromoBanner_V3Impl.class);

        // Load mock content from test JSON
        ctx.load().json("/promobanner-v3/PromoBanner_V3ImplTest.json", "/content");
    }

    /**
     * Verifies all properties of the PromoBanner_V3Impl model in a single test.
     * Ensures that text fields (text1, text2, text3), button label, and button link
     * return expected values from the mock JSON content.
     * This test helps validate the correctness of dialog field mappings.
     */

    @Test
    void testPromoBannerValues() {
        ctx.currentResource("/content/promobannerv3");
        PromoBanner_V3Impl banner = ctx.currentResource().adaptTo(PromoBanner_V3Impl.class);
        assertNotNull(banner);
        assertEquals("HOW IT'S MADE:", banner.getText1());
        assertEquals("DESIGNED FROM NATURAL GASHOW IT'S MADE:", banner.getText2());
        assertEquals("SHELL HELIX ULTRA WITH PUREPLUS TECHNOLOGY", banner.getText3());
        assertEquals("START", banner.getButtonText());
        assertEquals("https://www.shell.in/fuels-oils-and-coolants/helix-for-cars/pureplus.html", banner.getButtonPath());
    }

    /**
     * Verifies that getText1() returns the correct first line of text.
     */
    @Test
    public void testText1() {
        ctx.currentResource("/content/promobannerv3");
        PromoBanner_V3Impl banner = ctx.currentResource().adaptTo(PromoBanner_V3Impl.class);
        String actual = banner.getText1();
        assertEquals("HOW IT'S MADE:", actual);
    }

    /**
     * Verifies that getText2() returns the correct second line of text.
     */
    @Test
    public void testText2() {
        ctx.currentResource("/content/promobannerv3");
        PromoBanner_V3Impl banner = ctx.currentResource().adaptTo(PromoBanner_V3Impl.class);
        assertNotNull(banner);
        String actual = banner.getText2();
        assertEquals("DESIGNED FROM NATURAL GASHOW IT'S MADE:", actual);
    }

    /**
     * Verifies that getText3() returns the correct third line of text.
     */
    @Test
    public void testText3() {
        ctx.currentResource("/content/promobannerv3");
        PromoBanner_V3Impl banner = ctx.currentResource().adaptTo(PromoBanner_V3Impl.class);
        assertNotNull(banner);
        String actual = banner.getText3();
        assertEquals("SHELL HELIX ULTRA WITH PUREPLUS TECHNOLOGY", actual);
    }

    /**
     * Verifies that getButtonText() returns the correct CTA button label.
     */
    @Test
    public void textbuttonText() {
        ctx.currentResource("/content/promobannerv3");
        PromoBanner_V3Impl banner = ctx.currentResource().adaptTo(PromoBanner_V3Impl.class);
        assertNotNull(banner);
        String actual = banner.getButtonText();
        assertEquals("START", actual);
    }

    /**
     * Verifies that getButtonPath() returns the correct CTA URL.
     */
    @Test
    public void testbuttonPath() {
        ctx.currentResource("/content/promobannerv3");
        PromoBanner_V3Impl banner = ctx.currentResource().adaptTo(PromoBanner_V3Impl.class);
        assertNotNull(banner);
        String actual = banner.getButtonPath();
        assertEquals("https://www.shell.in/fuels-oils-and-coolants/helix-for-cars/pureplus.html", actual);
    }
}