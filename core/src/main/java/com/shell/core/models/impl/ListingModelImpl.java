package com.shell.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.shell.core.models.Listing;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = Listing.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ListingModelImpl implements Listing {

    @ValueMapValue
    private String articlePagePath;

    @Self
    private Resource currentResource;

    private List<ArticleItem> articles;

    @PostConstruct
    protected void init() {
        articles = new ArrayList<>();

        if (articlePagePath != null && !articlePagePath.isEmpty()) {
            ResourceResolver resolver = currentResource.getResourceResolver();
            Resource parentResource = resolver.getResource(articlePagePath);

            if (parentResource != null) {
                for (Resource child : parentResource.getChildren()) {
                    Page page = child.adaptTo(Page.class);
                    if (page != null) {
                        String imagePath = null;
                        Resource contentResource = page.getContentResource();

                        if (contentResource != null) {
                            for (Resource resChild : contentResource.getChildren()) {
                                if ("image".equals(resChild.getName())) {
                                    imagePath = resChild.getValueMap().get("fileReference", String.class);
                                    break;
                                }
                            }
                        }

                        articles.add(new ArticleItem(
                                page.getTitle(),
                                page.getDescription(),
                                page.getPath(),
                                imagePath
                        ));
                    }
                }
            }
        }
    }

    @Override
    public List<ArticleItem> getArticles() {
        return articles;
    }
}
