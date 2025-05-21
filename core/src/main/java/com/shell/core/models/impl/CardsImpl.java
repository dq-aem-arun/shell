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
}
