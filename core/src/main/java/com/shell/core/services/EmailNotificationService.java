package com.shell.core.services;

import java.util.Date;

public interface EmailNotificationService {
    void sendEmail(String path, String title, String action, String user, Date date);
    
}
