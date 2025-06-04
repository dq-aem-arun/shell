package com.shell.core.models.impl;

import com.shell.core.models.Cards;
import com.shell.core.models.CardsItems;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Sling Model implementation of the Cards interface.
 * Fetches the list of card items from the content repository.
 *
 * @author Saraswathi
 * @version 1.0
 * @since 2025-05-21
 */
@Model(adaptables = Resource.class, adapters = Cards.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardsImpl implements Cards {

    // Logger for debugging and tracing model behavior
    private static final Logger LOG = LoggerFactory.getLogger(CardsImpl.class);

    /**
     * Injects the list of child resources mapped to CardsItems model.
     */
    @ChildResource
    private List<CardsItems> cards;

    /**
     * Returns the list of card items.
     * Logs the number of cards fetched.
     */
    @Override
    public List<CardsItems> getCards() {
        LOG.info("Fetching card list. Total cards: {}", cards != null ? cards.size() : 0);
        return cards;
    }
    /**
     * Checks whether the card component is empty.
     * This method returns {@code true} if the list of cards is either {@code null} or contains no elements.
     * Useful for determining whether to render the component or display a placeholder in the authoring interface.
     *
     * @return {@code true} if there are no cards to display; {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        boolean isEmpty = cards == null || cards.isEmpty();
        LOG.info("Checking if the component is empty: {}", isEmpty);
        return isEmpty;
    }

}