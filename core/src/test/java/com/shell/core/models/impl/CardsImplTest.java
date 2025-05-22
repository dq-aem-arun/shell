package com.shell.core.models.impl;

import com.shell.core.models.CardsItems;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test class for {@link CardsImpl} using AEM Mocks and JUnit 5.
 * Validates model behavior and JSON bindings for various use cases.
 */
@ExtendWith(AemContextExtension.class)
public class CardsImplTest {

    // AEM context simulates the AEM environment for unit testing
    private final AemContext context = new AemContext();

    /**
     * Registers model classes and loads test content before each test.
     */
    @BeforeEach
    public void setUp() {
        context.addModelsForClasses(CardsImpl.class, CardsItemsImpl.class);
        context.load().json("/cards/CardsImplTest.json", "/content");
    }

    /**
     * Verifies that the model adapts correctly and is not null.
     */
    @Test
    @DisplayName("testing title")
    public void testNotNull() {
        context.currentResource("/content/cardsList");
        CardsImpl cards = context.currentResource().adaptTo(CardsImpl.class);
        assertNotNull(cards);
    }

    /**
     * Tests that the first card's title is properly fetched from JSON.
     */
    @Test
    public void testTitle() {
        context.currentResource("/content/cardsList");
        CardsImpl cards = context.currentResource().adaptTo(CardsImpl.class);
        List<CardsItems> card = cards.getCards();
        String actual = card.get(0).getTitle();
        assertNotNull(cards);
        assertNotNull(card);
        assertNotNull(actual);
        assertEquals("AEM", actual);
    }

    /**
     * Tests all fields for the cards to ensure correct mapping.
     */
    @Test
    public void testAllFields() {
        context.currentResource("/content/cardsList");
        CardsImpl cards = context.currentResource().adaptTo(CardsImpl.class);
        List<CardsItems> listofcards = cards.getCards();
        CardsItems item0 = listofcards.get(0);
        String title = item0.getTitle();
        String des = item0.getDescription();
        String pathofImage = item0.getFileReference();
        boolean showbutton = item0.getShowButton();
        assertEquals("AEM", title);
        assertEquals("hello", des);
        assertEquals("/content/dam/shell/image1.jpg", pathofImage);
        assertEquals(true, showbutton);
        // Validate values for first card item
        CardsItems item1 = listofcards.get(1);
        String title1 = item1.getTitle();
        String des1 = item1.getDescription();
        String pathofImage1 = item1.getFileReference();
        // Validate values for second card item
        assertEquals("Hello", title1);
        assertEquals("welcome", des1);
        assertEquals("/content/dam/shell/image2.jpg", pathofImage1);
    }

    /**
     * Validates the isDescriptionEmpty method when description is missing.
     */
    @Test
    public void testDescriptionIsEmpty() {
        context.currentResource("/content/cardsList2");
        CardsImpl cards = context.currentResource().adaptTo(CardsImpl.class);
        List<CardsItems> listofcards = cards.getCards();
        // Check if the description is correctly flagged as empty
        CardsItems item = listofcards.get(0);
        assertTrue(item.isDesscriptionEmpty());
    }

    /**
     * Verifies behavior when card fields are missing or null.
     */
    @Test
    @DisplayName("cards does not have any field value")
    public void cardsListWithMissingFields() {
        context.currentResource("/content/cardsListWithMissingFields");
        CardsImpl cards = context.currentResource().adaptTo(CardsImpl.class);
        List<CardsItems> listofcards = cards.getCards();
        // All fields should be null or default
        CardsItems item = listofcards.get(0);
        assertNull(item.getTitle());
        assertNull(item.getDescription());
        assertNull(item.getFileReference());
    }
}
