/**
 * EmailNotificationService
 * 
 * @author Jay 
 * 
 * Interface for sending email notifications.
 */
package com.shell.core.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EmailNotificationService {
    void sendEmail(String path, String title, String action, String user, Date date);
    void sendEmail(List<Map<String, String>> data,String status);
}
