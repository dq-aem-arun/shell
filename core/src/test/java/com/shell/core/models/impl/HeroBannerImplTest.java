package com.shell.core.models.impl;

import com.shell.core.models.HeroBanner;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HeroBannerImplTest {

    AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroBanner.class);
        context.load().json("/heroBanner/herobanner.json",
                "/component");
    }

    @Test
    void getTitle() {
        context.currentResource("/component/container/shell_herobanner");
        HeroBanner heroBanner = context.currentResource().adaptTo(HeroBanner.class);
        assertNotNull(heroBanner);

        String expected = "Every journey has a plus with Shell Go+";
        String actual = heroBanner.getTitle();
        assertEquals(expected,actual);

    }

    @Test
    void getDetails() {
        context.currentResource("/component/container/shell_herobanner");
        HeroBanner heroBanner = context.currentResource().adaptTo(HeroBannerImpl.class);
        assertNotNull(heroBanner);

        String expected = "Make your journeys on and off the road more rewarding with the Shell Asia app.";
        String actual = heroBanner.getDetails();
        assertEquals(expected,actual);
    }

    @Test
    void getImage() {
        context.currentResource("/component/container/shell_herobanner");
        HeroBannerImpl heroBanner = context.currentResource().adaptTo(HeroBannerImpl.class);
        assertNotNull(heroBanner);

        String expected = "/content/dam/shell/shell-go-plus-india-banner.jpeg";
        String actual = heroBanner.getImage();
        assertEquals(expected,actual,"image test failed!!");
        assertNotEquals("/content/dam/shell/shell-go-plus-india-banner.jpe",heroBanner.getImage());

        assertSame(expected.intern(),actual.intern());
    }


}