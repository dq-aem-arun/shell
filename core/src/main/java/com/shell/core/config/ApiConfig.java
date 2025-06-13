package com.shell.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "APIConfiguration", description = "Configuration for external API URL")
public @interface ApiConfig {

    @AttributeDefinition(
            name = "API URL",
            description = "Base URL for the external API (e.g., https://jsonplaceholder.typicode.com/posts)"
    )
    String apiUrl() default "https://jsonplaceholder.typicode.com/posts";
}
