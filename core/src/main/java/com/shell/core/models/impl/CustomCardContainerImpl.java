package com.shell.core.models.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ComponentExporter;
import com.shell.core.models.CustomCardContainer;

@Model(
    adaptables = Resource.class,
    adapters = { CustomCardContainer.class, ComponentExporter.class },
    resourceType = "your-project/components/content/customcardcontainer",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomCardContainerImpl implements CustomCardContainer {


    @Self
    private Resource resource;


    @ValueMapValue
    private String heading;

    @ValueMapValue(name = "fileReference")
    private String image;

    @ValueMapValue
    @Default(intValues = 3)
    private int numberOfCards;

    @Override
    public String getHeading() {
        return heading;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public int getNumberOfCards() {
        return numberOfCards;
    }

    @Override
    public String getExportedType() {
        return "shell/components/content/customcardcontainer";
    }

    public List<Integer> getCardIndices() {
        return IntStream.range(0, numberOfCards).boxed().collect(Collectors.toList());
    }

    @Override
    public List<String> getCardPaths() {
        List<String> paths = new ArrayList<>();

        for (int i = 1; i <= numberOfCards; i++) { // ✅ Start from 1
            Resource card = resource.getChild("card_" + i);
            if (card != null && !card.getPath().equals(resource.getPath())) { // ✅ safety
                paths.add(card.getPath());
            }
        }

        return paths;
    }

    @Override
    public boolean isEmpty() {
        return heading == null || heading.isEmpty();
    }
}
