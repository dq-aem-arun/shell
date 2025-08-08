package com.shell.core.models;

public interface CookieConsent {
    public String getTitle();
    public String getDescription();
    public String getAcceptText();
    public String getRejectText();
    public String getPolicyLink();
    public boolean isEmpty();
}
