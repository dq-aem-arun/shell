package com.shell.core.models.impl;

import com.shell.core.testcontext.AppAemContext;
import com.shell.core.models.ShellButtonItem;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the ShellButtonImpl model.
 * This class tests the functionality of the ShellButtonImpl class, including retrieving button data
 * and verifying the behavior of the isEmpty() method.
 */
@ExtendWith(AemContextExtension.class)
public class ShellButtonImplTest {

    // AEM context for setting up the test environment
    private final AemContext ctx = AppAemContext.newAemContext();

    /**
     * Sets up the test environment before each test.
     * Registers the ShellButtonImpl model and loads the test JSON content.
     */
    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(ShellButtonImpl.class);
        ctx.load().json("/shellbutton/ShellButtonImplTest.json", "/content");
    }

    /**
     * Tests that the ShellButtonImpl model is not null when adapted from a resource.
     */
    @Test
    void testShellButtonModelNotNull() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertNotNull(shellButton);
    }

    /**
     * Tests the retrieval of button data from the ShellButtonImpl model.
     * Verifies the text, link, and icon for multiple buttons.
     */
    @Test
    void testGetButtons() {
        ctx.currentResource("/content/shell-buttons");

        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertNotNull(shellButton);

        // Verify the first button's properties
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("Button 1", button1.getButtonText());
        assertEquals("shell/us/en/home-page-1", button1.getButtonLink());
        assertEquals("content/shell/images/icons/icon-1.png", button1.getIcon());

        // Verify the second button's properties
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("Button 2", button2.getButtonText());
        assertEquals("shell/us/en/home-page-2", button2.getButtonLink());
        assertEquals("content/shell/images/icons/icon-2.png", button2.getIcon());
    }

    /**
     * Tests the text of the first button.
     * Verifies that the text matches the expected value.
     */
    @Test
    void testButton1Text() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("Button 1", button1.getButtonText());
    }

    /**
     * Tests the link of the first button.
     * Verifies that the link matches the expected value.
     */
    @Test
    void testButton1Link() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("shell/us/en/home-page-1", button1.getButtonLink());
    }

    /**
     * Tests the icon of the first button.
     * Verifies that the icon path matches the expected value.
     */
    @Test
    void testButton1Icon() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("content/shell/images/icons/icon-1.png", button1.getIcon());
    }

    /**
     * Tests the text of the second button.
     * Verifies that the text matches the expected value.
     */
    @Test
    void testButton2Text() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("Button 2", button2.getButtonText());
    }

    /**
     * Tests the link of the second button.
     * Verifies that the link matches the expected value.
     */
    @Test
    void testButton2Link() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("shell/us/en/home-page-2", button2.getButtonLink());
    }

    /**
     * Tests the icon of the second button.
     * Verifies that the icon path matches the expected value.
     */
    @Test
    void testButton2Icon() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("content/shell/images/icons/icon-2.png", button2.getIcon());
    }

    /**
     * Tests that the isEmpty() method returns true when no buttons are configured.
     * Verifies that the component is considered empty.
     */
    @Test
    void testIsEmptyReturnsTrue() {
        ctx.currentResource("/content/shell-buttonsEmpty");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertTrue(shellButton.isEmpty());
    }

    /**
     * Tests that the buttons list is empty when no buttons are configured.
     * Verifies that the list size is zero.
     */
    @Test
    void testButtonsListIsEmpty() {
        ctx.currentResource("/content/shell-buttonsEmpty");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertEquals(0, shellButton.getButtons().size());
    }
}
