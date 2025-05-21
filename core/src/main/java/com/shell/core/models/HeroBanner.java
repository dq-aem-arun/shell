package com.shell.core.models;

public interface HeroBanner {
    String RESOURCE_TYPE = "shell/components/content/shell-herobanner";

    String getTitle();

    String getDetails();

    String getImage();
}