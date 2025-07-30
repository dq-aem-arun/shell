package com.shell.core.models;

import com.shell.core.models.CTACardItems;
import com.shell.core.models.impl.CTACardsImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the CTACardsImpl Sling Model.
 * Uses AEM Mocks (AemContext) to simulate the AEM environment.
 *
 * @author Saraswathi
 * @version 1.0
 * @since 22-05-2025
 */
@ExtendWith({AemContextExtension.class})
public class CTACardsImplTest {
    // AEM context used for simulating the AEM runtime environment
    private final AemContext context=new AemContext();

    /**
     * Initializes the test environment before each test case.
     * - Registers the model classes (CTACardsImpl and CTACardItemsImpl)
     * - Loads mock JSON content into the AEM context under /content
     */
    @BeforeEach
    public void setUp(){
       context.addModelsForClasses(CTACardsImpl.class,CTACardItemsImpl.class);
       context.load().json("/ctacard/CTACardsImplTest.json","/content");

    }

    /**
     * Test to verify that the adapted CTACardsImpl model is not null
     * when retrieved from a valid resource.
     */
    @Test
    public void texstCtaCardAreNotNull(){
        context.currentResource("/content/ctaCardsList");
        CTACardsImpl ctacard=context.currentResource().adaptTo(CTACardsImpl.class);
        assertNotNull(ctacard);
    }

    /**
     * Test to validate that the fields in the first card item (iconReference, title) are not null.
     */
    @Test
    public void testEachFieldNotNull(){
        context.currentResource("/content/ctaCardsList");
        CTACardsImpl ctacard=context.currentResource().adaptTo(CTACardsImpl.class);
        List<CTACardItems> items = ctacard.getCtaCards();
        CTACardItems item=items.get(0);
        String iconpath=item.getIconReference();
        String title=item.getTitle();
        assertNotNull(iconpath);
        assertNotNull(title);
    }

    /**
     * Test to verify that the fields in the first card match the expected values
     * from the JSON fixture.
     */
    @Test
    public void testFieldsWithExpectedInputs(){
        context.currentResource("/content/ctaCardsList");
       CTACardsImpl cards = context.currentResource().adaptTo(CTACardsImpl.class);
        List<CTACardItems> items = cards.getCtaCards();
        CTACardItems item0 = items.get(0);
        String iconpath=item0.getIconReference();
        String title=item0.getTitle();
        assertEquals("/content/dam/shell/image1.jpg",iconpath);
        assertEquals("AEM",title);
    }

    /**
     * Test to validate that card list is empty or not
     */
    @Test
    public void testCtaCardsListEmpty(){
        context.currentResource("/content/ctaCardsList3");
        CTACardsImpl ctacards=  context.currentResource().adaptTo(CTACardsImpl.class);
        List<CTACardItems> cards = ctacards.getCtaCards();
        assertEquals(true,cards.isEmpty());
        assertTrue(cards.isEmpty());
    }

    /**
     * Test to validate that when the card fields are missing in the JSON data,
     * the model returns null for those fields.
     */
    @Test
    public void ctaCardsWithEmptyFields(){
        context.currentResource("/content/ctaCardsList1");
        CTACardsImpl cards = context.currentResource().adaptTo(CTACardsImpl.class);
        List<CTACardItems> card = cards.getCtaCards();
        CTACardItems item0=card.get(0);
        String iconpath=item0.getIconReference();
        String title=item0.getTitle();
        assertNull(iconpath);
        assertNull(title);
    }
}
