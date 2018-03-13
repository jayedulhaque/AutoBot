package com.mapler.nexmo;

/**
 *
 * @author JAYED
 */
public class NModel {

    String files;
    String message;
    long waitTimeInSec;
    //String id;
    //String id_str;
    //String number;    
    //int limit;   
    //long sentCount;
    //long lastSentTimeInSec;
    //String messageId;
    

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public long getWaitTimeInSec() {
        return waitTimeInSec;
    }

    public void setWaitTimeInSec(long waitTimeInSec) {
        this.waitTimeInSec = waitTimeInSec;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
