package com.mapler.utility;

/**
 *
 * @author none
 */
public class EMailModel {

    private String email;
    private String emailPassword;
    private String mailServer;
    private String protocal;
    private boolean mailReadAndUpdateOnly;

    public boolean isMailReadAndUpdateOnly() {
        return mailReadAndUpdateOnly;
    }

    public void setMailReadAndUpdateOnly(boolean mailReadAndUpdateOnly) {
        this.mailReadAndUpdateOnly = mailReadAndUpdateOnly;
    }
    
    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }
}
