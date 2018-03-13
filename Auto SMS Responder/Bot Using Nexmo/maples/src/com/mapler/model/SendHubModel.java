package com.mapler.model;

/**
 *
 * @author none
 */
public class SendHubModel {

    String id;
    String id_str;
    String number;
    String message;
    int limit;
    int waitTimeInSec;
    long sentCount;
    long lastSentTimeInSec;
    String messageId;
    String files;

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
    
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    
    
    public long getSentCount() {
        return sentCount;
    }

    public void setSentCount(long sentCount) {
        this.sentCount = sentCount;
    }

    public long getLastSentTimeInSec() {
        return lastSentTimeInSec;
    }

    public void setLastSentTimeInSec(long lastSentTimeInSec) {
        this.lastSentTimeInSec = lastSentTimeInSec;
    }

    public int getWaitTimeInSec() {
        return waitTimeInSec;
    }

    public void setWaitTimeInSec(int waitTimeInSec) {
        this.waitTimeInSec = waitTimeInSec;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
