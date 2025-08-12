package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.shell.core.models.CookieConsent;

/**
 * Author: Mohammad Shoaib
 * Date: 2025-08-06
 * 
 * 
 * Implementation of the {@link CookieConsent} interface.
 * 
 * <p>This Sling model adapts from a {@link Resource} and provides the data for a cookie consent banner,
 * including title, description, accept/reject text, and a link to the cookie policy.</p>
 */
@Model(
    adaptables = Resource.class,
    adapters = CookieConsent.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CookieConsentImpl implements CookieConsent {

    /**
     * Title of the cookie consent banner.
     */
    @ValueMapValue
    private String title;

    /**
     * Description text of the cookie consent banner.
     */
    @ValueMapValue
    private String description;

    /**
     * Text displayed on the 'Accept' button.
     */
    @ValueMapValue
    private String acceptText;

    /**
     * Text displayed on the 'Reject' button.
     */
    @ValueMapValue
    private String rejectText;

    /**
     * Link to the cookie policy page.
     */
    @ValueMapValue
    private String policyLink;

    /**
     * Returns the title of the cookie consent.
     *
     * @return the title text
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the cookie consent.
     *
     * @return the description text
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the text for the accept button.
     *
     * @return the accept button text
     */
    @Override
    public String getAcceptText() {
        return acceptText;
    }

    /**
     * Returns the text for the reject button.
     *
     * @return the reject button text
     */
    @Override
    public String getRejectText() {
        return rejectText;
    }

    /**
     * Returns the cookie policy link with ".html" extension.
     * If the original link ends with a slash, it is removed.
     *
     * @return the formatted policy link
     */
    @Override
    public String getPolicyLink() {
        if (policyLink != null && policyLink.endsWith("/")) {
            policyLink = policyLink.substring(0, policyLink.length() - 1);
        }
        return policyLink != null ? policyLink + ".html" : null;
    }

    /**
     * Checks whether all properties are empty or null.
     *
     * @return {@code true} if all fields are empty; {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return (title == null || title.isEmpty()) &&
               (description == null || description.isEmpty()) &&
               (acceptText == null || acceptText.isEmpty()) &&
               (rejectText == null || rejectText.isEmpty()) &&
               (policyLink == null || policyLink.isEmpty());
    }
}
