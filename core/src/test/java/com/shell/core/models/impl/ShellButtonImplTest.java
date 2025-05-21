package com.shell.core.models.impl;

import com.shell.core.testcontext.AppAemContext;
import com.shell.core.models.ShellButton.ShellButtonItem;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class ShellButtonImplTest {

    private final AemContext ctx = AppAemContext.newAemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(ShellButtonImpl.class);
        ctx.load().json("/shellbutton/ShellButtonImplTest.json", "/content");
    }

    @Test
    void testShellButtonModelNotNull() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertNotNull(shellButton);
    }


    @Test
    void testGetButtons() {


        ctx.currentResource("/content/shell-buttons");

        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertNotNull(shellButton);
        ShellButtonItem button1 = shellButton.getButtons().get(0);


        assertEquals("Button 1", button1.getButtonText());
        assertEquals("shell/us/en/home-page-1", button1.getButtonLink());
        assertEquals("content/shell/images/icons/icon-1.png", button1.getIcon());

        ShellButtonItem button2 = shellButton.getButtons().get(1);


        assertEquals("Button 2", button2.getButtonText());
        assertEquals("shell/us/en/home-page-2", button2.getButtonLink());
        assertEquals("content/shell/images/icons/icon-2.png", button2.getIcon());

    }

    @Test
    void testButton1Text() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("Button 1", button1.getButtonText());
    }

    @Test
    void testButton1Link() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("shell/us/en/home-page-1", button1.getButtonLink());
    }

    @Test
    void testButton1Icon() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button1 = shellButton.getButtons().get(0);
        assertEquals("content/shell/images/icons/icon-1.png", button1.getIcon());
    }

    @Test
    void testButton2Text() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("Button 2", button2.getButtonText());
    }

    @Test
    void testButton2Link() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("shell/us/en/home-page-2", button2.getButtonLink());
    }

    @Test
    void testButton2Icon() {
        ctx.currentResource("/content/shell-buttons");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        ShellButtonItem button2 = shellButton.getButtons().get(1);
        assertEquals("content/shell/images/icons/icon-2.png", button2.getIcon());
    }

    @Test
    void testIsEmptyReturnsTrue() {
        ctx.currentResource("/content/shell-buttonsEmpty");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertTrue(shellButton.isEmpty());
    }

    @Test
    void testButtonsListIsEmpty() {
        ctx.currentResource("/content/shell-buttonsEmpty");
        ShellButtonImpl shellButton = ctx.currentResource().adaptTo(ShellButtonImpl.class);
        assertEquals(0, shellButton.getButtons().size());
    }
}
