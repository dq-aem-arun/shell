package com.shell.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class Listing {

    @ValueMapValue
    private String parentPagePath;

    @Self
    private Resource resource;

    private List<Page> articles = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (parentPagePath != null) {
            PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
            Page parentPage = pageManager.getPage(parentPagePath);

            if (parentPage != null) {
                parentPage.listChildren().forEachRemaining(articles::add);
            }
        }
    }

    public List<Page> getArticles() {
        return new ArrayList<>(articles);
    }
}