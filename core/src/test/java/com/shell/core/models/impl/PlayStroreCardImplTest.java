package com.shell.core.models.impl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class PlayStroreCardImplTest {

    private final AemContext context = new AemContext();
    private PlayStroreCardImpl playStroreCardImpl;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(PlayStroreCardImpl.class);
        context.load().json("/playstorecard/PlayStroreCardImplTest.json", "/content");
        context.currentResource("/content");
        playStroreCardImpl = context.currentResource().adaptTo(PlayStroreCardImpl.class);
    }

    @Test
    void testPlayStoreCardModel() {
        assertNotNull(playStroreCardImpl);
        assertEquals("Download Our App", playStroreCardImpl.getTitlefield());
        assertEquals("Get it from Google Play or App Store", playStroreCardImpl.getDescription());
        assertEquals("/content/dam/images/google-play-badge.png", playStroreCardImpl.getFirstImageReference());
        assertEquals("https://example.com", playStroreCardImpl.getFirstImageLinkURL());
        assertEquals("/content/dam/images/app-store-badge.png", playStroreCardImpl.getSecondImageReference());
        assertEquals("https://example.com", playStroreCardImpl.getSecondImageLinkURL());
    }

    @Test
    void testGetTitlefield() {
        assertNotNull(playStroreCardImpl);
        assertEquals("Download Our App", playStroreCardImpl.getTitlefield());
    }

    @Test
    void testGetDescription() {
        assertNotNull(playStroreCardImpl);
        assertEquals("Get it from Google Play or App Store", playStroreCardImpl.getDescription());
    }

    @Test
    void testGetFirstImageReference() {
        assertNotNull(playStroreCardImpl);
        assertEquals("/content/dam/images/google-play-badge.png", playStroreCardImpl.getFirstImageReference());
    }

    @Test
    void testGetFirstImageLinkURL() {
        assertNotNull(playStroreCardImpl);
        assertEquals("https://example.com", playStroreCardImpl.getFirstImageLinkURL());
    }

    @Test
    void testGetSecondImageReference() {
        assertNotNull(playStroreCardImpl);
        assertEquals("/content/dam/images/app-store-badge.png", playStroreCardImpl.getSecondImageReference());
    }

    @Test
    void testGetSecondImageLinkURL() {
        assertNotNull(playStroreCardImpl);
        assertEquals("https://example.com", playStroreCardImpl.getSecondImageLinkURL());
    }
}
