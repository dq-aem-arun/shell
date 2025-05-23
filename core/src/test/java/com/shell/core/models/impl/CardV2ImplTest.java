package com.shell.core.models.impl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CardV2ImplTest {

    private final AemContext context = new AemContext();
    private CardV2Impl cardV2Impl;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CardV2Impl.class);
        context.load().json("/cardv2/CardV2ImplTest.json", "/content");
        context.currentResource("/content");
        cardV2Impl = context.currentResource().adaptTo(CardV2Impl.class);
    }

    @Test
    void testCardV2Model() {
        assertNotNull(cardV2Impl);
        assertEquals("Earn points on every visit", cardV2Impl.getTitle());
        assertEquals("/content/dam/shell/cards/visit-points.png", cardV2Impl.getImageReference());
    }

    @Test
    void testGetTitle() {
        assertNotNull(cardV2Impl);
        assertEquals("Earn points on every visit", cardV2Impl.getTitle());
    }

    @Test
    void testGetImageReference() {
        assertNotNull(cardV2Impl);
        assertEquals("/content/dam/shell/cards/visit-points.png", cardV2Impl.getImageReference());
    }
}