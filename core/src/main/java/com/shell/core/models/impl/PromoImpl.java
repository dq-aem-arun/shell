package com.shell.core.models.impl;

import com.shell.core.models.Promobanner;
import com.shell.core.models.Promo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import java.util.List;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

@Model(adaptables = Resource.class, adapters = Promo.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PromoImpl implements Promo {

    // Injecting the child resources under the "promobanner" node
    @ChildResource(name = "promobanner")
    private List<Promobanner> promobanner;


    public List<Promobanner> getPromoBanner() {
        return promobanner;
    }
}
