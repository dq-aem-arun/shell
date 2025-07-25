/**
 * EmailConfig
 * 
 * @author Jay
 * 
 * OSGi configuration interface for email notification settings.
 */
package com.shell.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "Email Notification Configuration",
    description = "Configuration for email notifications"
)
public @interface EmailConfig {

    @AttributeDefinition(name = "From Address", description = "Email sender address")
    String fromAddress();

    @AttributeDefinition(name = "To Address", description = "Email recipient address list")
    String[] toAddress() default {"punyanijay@gmail.com"};

    @AttributeDefinition(name = "Subject Prefix", description = "Prefix for email subject")
    String subjectPrefix() default "Subject Line Prefix";
}
