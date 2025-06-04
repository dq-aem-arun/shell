/**
 * EmailNotificationServiceImpl
 * 
 * @author Jay Punyani
 * 
 * This class implements the EmailNotificationService to send email notifications
 * using the configured MessageGatewayService and EmailConfig.
 **/
package com.shell.core.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public void sendEmail(List<Map<String, String>> data, String status) {
        HtmlEmail email = new HtmlEmail();
        email.setSubject(config.subjectPrefix() + ": " + status);

        try {
            String body = buildNewsEventEmailBody(data, status);
            email.setHtmlMsg(body);
            email.setFrom(config.fromAddress());

            for (String to : config.toAddress()) {
                email.addTo(to);
            }

            MessageGateway<Email> gateway = messageGatewayService.getGateway(HtmlEmail.class);
            if (gateway != null) {
                gateway.send(email);
                LOG.info("Email sent successfully for {} event with {} item(s).", status, data.size());
            } else {
                LOG.error("No MessageGateway found for HtmlEmail. Email not sent for {} event.", status);
            }

        } catch (EmailException e) {
            LOG.error("Failed to send email notification for {} event. Error: {}", status, e.getMessage());
        }
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


    public static String buildNewsEventEmailBody(List<Map<String, String>> data, String status) {
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<html><body>");

        // Determine color based on status
        String color;
        switch (status.toLowerCase()) {
            case "created":
                color = "#28a745"; // green
                break;
            case "updated":
                color = "#ffc107"; // yellow
                break;
            case "deleted":
                color = "#dc3545"; // red
                break;
            default:
                color = "#000"; // fallback black
                break;
        }

        // Header
        bodyBuilder.append(String.format(
                "<h2 style='color:%s;'>News page %s event</h2>", color, status.toLowerCase()));

        // Table start
        bodyBuilder.append("<table style='border-collapse: collapse; width: 100%;'>");
        bodyBuilder.append("<thead><tr>")
                .append("<th style='border: 1px solid #ccc; padding: 8px; background:#f4f4f4;'>Title</th>")
                .append("<th style='border: 1px solid #ccc; padding: 8px; background:#f4f4f4;'>Path</th>")
                .append("<th style='border: 1px solid #ccc; padding: 8px; background:#f4f4f4;'>User</th>")
                .append("<th style='border: 1px solid #ccc; padding: 8px; background:#f4f4f4;'>Date</th>")
                .append("</tr></thead><tbody>");

        // Table rows
        for (Map<String, String> entry : data) {
            String title = entry.getOrDefault("title", "N/A");
            String path = entry.getOrDefault("path", "N/A");
            String user = entry.getOrDefault("author", "N/A");
            String date = entry.getOrDefault("date", "N/A");

            bodyBuilder.append("<tr>")
                    .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(title).append("</td>")
                    .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(path).append("</td>")
                    .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(user).append("</td>")
                    .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(date).append("</td>")
                    .append("</tr>");
        }

        bodyBuilder.append("</tbody></table>");
        bodyBuilder.append("<p style='margin-top:20px;'>This is an automated message from AEM News Page Monitoring.</p>");
        bodyBuilder.append("</body></html>");

        return bodyBuilder.toString();
    }

}
