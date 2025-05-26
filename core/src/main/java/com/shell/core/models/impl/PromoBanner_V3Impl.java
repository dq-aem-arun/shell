package com.shell.core.models.impl;

import com.shell.core.models.PromoBanner_V3;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model implementation of the PromoBanner_V3 interface.
 * It adapts from a Resource and reads banner text and button properties
 * from the resource's value map.
 * <p>
 * This model is typically used to display promotional banners with
 * three lines of text and a  button.
 * </p>
 *
 * @author Saraswathi
 * @version 1.0
 * @since 26-05-2025
 */
@Model(
        adaptables = Resource.class,
        adapters = PromoBanner_V3.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PromoBanner_V3Impl implements PromoBanner_V3 {

    /** SLF4J Logger for logging messages **/
    private static final Logger LOG = LoggerFactory.getLogger(PromoBanner_V3Impl.class);

    @ValueMapValue
    private String text1;

    @ValueMapValue
    private String text2;

    @ValueMapValue
    private String text3;

    @ValueMapValue
    private String buttonText;

    @ValueMapValue
    private String buttonPath;

    /**
     * Gets the first line of banner text.
     * @return the first banner text
     */
    @Override
    public String getText1() {
        LOG.info("getText1() called - value: {}", text1);
        return text1;
    }

    /**
     * Gets the second line of banner text.
     * @return the second banner text
     */
    @Override
    public String getText2() {
        LOG.info("getText2() called - value: {}", text2);
        return text2;
    }

    /**
     * Gets the third line of banner text.
     * @return the third banner text
     */
    @Override
    public String getText3() {
        LOG.info("getText3() called - value: {}", text3);
        return text3;
    }

    /**
     * Gets the button text for the CTA.
     * @return the CTA button label
     */
    @Override
    public String getButtonText() {
        LOG.info("getButtonText() called - value: {}", buttonText);
        return buttonText;
    }

    /**
     * Gets the path (URL) that the CTA button should navigate to.
     * @return the CTA button link path
     */
    @Override
    public String getButtonPath() {
        LOG.info("getButtonPath() called - value: {}", buttonPath);
        return buttonPath;
    }
    /**
     * Determines whether the PromoBanner component has any meaningful content.
     * Returns true if all fields (text1, text2, text3, buttonText, buttonPath)
     * are either null or empty, indicating the component is effectively empty.
     * This method helps in controlling the display of the component name in author mode.
     *
     * @return true if all fields are empty; false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return text1==null||text1.isEmpty() && text2==null||text2.isEmpty() && text3==null||text3.isEmpty() && buttonText==null||buttonText.isEmpty() && buttonPath==null||buttonPath.isEmpty();
    }
}
