package com.shell.core.models;

import java.util.List;

import com.adobe.cq.export.json.ComponentExporter;

public interface CustomCardContainer extends ComponentExporter {

    String getHeading();

    String getImage();

    int getNumberOfCards();

    List<String> getCardPaths();

    boolean isEmpty();

}

