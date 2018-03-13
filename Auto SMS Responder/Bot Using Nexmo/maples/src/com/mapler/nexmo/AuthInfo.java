package com.mapler.nexmo;

/**
 *
 * @author JAYED
 */
public class AuthInfo {

    private String id;
    private int getMessage;
    private int sentMessage;

    public int getGetMessage() {
        return getMessage;
    }

    public void setGetMessage(int getMessage) {
        this.getMessage = getMessage;
    }

    public int getSentMessage() {
        return sentMessage;
    }

    public void setSentMessage(int sentMessage) {
        this.sentMessage = sentMessage;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String apiKey;
    private String apiSecret;
    private String number;
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
