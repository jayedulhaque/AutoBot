package com.mapler.nexmo;

/**
 *
 * @author JAYED
 */
public class NexmoModel {

    private String messageFile;
//    private String email;
//    private String emailPassword;
    private String username;
    private String password;
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    //private int limit;
    private boolean stopEngine;
    private boolean repeated = false;
    private int waitTime = 0;
    private int repeatTime;

    public int getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

//    public int getLimit() {
//        return limit;
//    }
//
//    public void setLimit(int limit) {
//        this.limit = limit;
//    }

    public boolean isStopEngine() {
        return stopEngine;
    }

    public void setStopEngine(boolean stopEngine) {
        this.stopEngine = stopEngine;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessageFile() {
        return messageFile;
    }

    public void setMessageFile(String messageFile) {
        this.messageFile = messageFile;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getEmailPassword() {
//        return emailPassword;
//    }
//
//    public void setEmailPassword(String emailPassword) {
//        this.emailPassword = emailPassword;
//    }

}
