package com.shell.core.services;

import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.Email;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.shell.core.config.EmailConfig;

@Component(
    service = EmailNotificationService.class,
    immediate = true,
    property = "service.description=Email Service"
)
@Designate(ocd = EmailConfig.class)
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    private EmailConfig config;

    @Activate
    @Modified
    protected void activate(EmailConfig config) {
        this.config = config;
    }

    @Override
    public void sendEmail(String path, String title, String action, String user, Date date) {
        HtmlEmail email = new HtmlEmail();
        email.setSubject(config.subjectPrefix() + ": " + title);

        String body = String.format(
            "<p>A news page has been <strong>%s</strong>.</p><p>Title: %s<br/>Path: %s<br/>User: %s<br/>Date: %s</p>",
            action.toLowerCase(), title, path, user, date.toString());

        try {
            email.setHtmlMsg(body);
            email.setFrom(config.fromAddress());

            for (String to : config.toAddress()) {
                email.addTo(to);
            }

            MessageGateway<Email> gateway = messageGatewayService.getGateway(HtmlEmail.class);
            if (gateway != null) {
                gateway.send(email);
                LOG.info("Email sent successfully for page: {} by user: {}", path, user);
            } else {
                LOG.error("No MessageGateway found for HtmlEmail. Email not sent for page: {}", path);
            }

        } catch (EmailException e) {
            LOG.error("Failed to send email notification for page: {}. Error: {}", path, e.getMessage());
        }
    }
}
