package com.shell.core.models.impl;

import com.shell.core.models.CardsItems;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model implementation for a single Card item.
 * Maps and exposes properties like image, title, description, and showButton.
 *
 * @author Saraswathi
 * @version 1.0
 * @since 2025-05-21
 */
@Model(adaptables = Resource.class, adapters = CardsItems.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardsItemsImpl implements CardsItems {
    // Logger for this model
    private static final Logger LOG = LoggerFactory.getLogger(CardsItemsImpl.class);

    // Image path or asset reference
    @ValueMapValue
    private String fileReference;

    // Title of the card
    @ValueMapValue
    private String title;

    // Description of the card
    @ValueMapValue
    private String description;

    // Flag indicating if the button should be displayed
    @ValueMapValue
    private boolean showButton;

    /**
     * @return true if the button should be shown.
     */
    @Override
    public boolean getShowButton() {
        LOG.info("Show button flag: {}", showButton);
        return showButton;
    }

    /**
     * @return image path for the card.
     */
    @Override
    public String getFileReference() {
        LOG.info("Getting fileReference: {}", fileReference);
        return fileReference;
    }

    /**
     * @return title of the card.
     */
    @Override
    public String getTitle() {
        LOG.info("Getting title: {}", title);
        return title;
    }

    /**
     * @return description of the card.
     */
    @Override
    public String getDescription() {
        LOG.info("Getting description: {}", description);
        return description;
    }

    /**
     * Checks if description is null or empty.
     *
     * @return true if description is empty or null.
     */
    @Override
    public boolean isDesscriptionEmpty() {
        boolean isEmpty = description == null || description.isEmpty();
        LOG.info("Checking if description is empty: {}", isEmpty);
        return isEmpty;
    }
}
