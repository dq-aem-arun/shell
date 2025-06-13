package com.shell.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Shell Scheduler Configuration", description = "Configuration for custom scheduler")
public @interface MySchedulerConfig {

    @AttributeDefinition(name = "Enable Scheduler", description = "Enable or disable the scheduler")
    boolean enable() default true;

    @AttributeDefinition(name = "Cron Expression", description = "Run every hour")
    String scheduler_expression() default "0 0 * * * ?"; // every hour

    @AttributeDefinition(name = "API URL", description = "API endpoint URL")
    String apiUrl() default "https://jsonplaceholder.typicode.com/posts";
}
