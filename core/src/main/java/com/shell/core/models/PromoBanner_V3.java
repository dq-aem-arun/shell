package com.shell.core.models;
/**
 * PromoBanner_V3 defines the contract for the Promo Banner component.
 * It includes methods to get the banner text and button details.
 * @author Saraswathi
 * @version 1.0
 * @since 26-05-2025
 */
public interface PromoBanner_V3 {

    //returns the text as string
    public String getText1();
    //returns the text as string
    public String getText2();
    //returns the text as string
    public String getText3();
    //returns the text as string
    public String getButtonText();
    //returns the text as string
    public String getButtonPath();
    //return the boolean if probanner v3 component not has the content
    public boolean isEmpty();
}
