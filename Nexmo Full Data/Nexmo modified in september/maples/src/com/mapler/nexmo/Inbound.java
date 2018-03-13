package com.mapler.nexmo;

/**
 *
 * @author JAYED
 */
public class Inbound {

    private int id;
    private String creation_date;
    private int message_status;
    private int send_count;
    private String message_id;
    private String account_id;
    private String message_from;
    private String message_to;
    private String body;
    private String date_recieved;
    private String message_type;
    private String network;
    private String user_id;
    private long next_send_time;
    private long last_send_time;
    private String api_key;
    private String api_secret;
    private long recieved_time;
    private String status_code;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getApi_secret() {
        return api_secret;
    }

    public void setApi_secret(String api_secret) {
        this.api_secret = api_secret;
    }

    public long getLast_send_time() {
        return last_send_time;
    }

    public void setLast_send_time(long last_send_time) {
        this.last_send_time = last_send_time;
    }

    public long getNext_send_time() {
        return next_send_time;
    }

    public void setNext_send_time(long next_send_time) {
        this.next_send_time = next_send_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public int getMessage_status() {
        return message_status;
    }

    public void setMessage_status(int message_status) {
        this.message_status = message_status;
    }

    public int getSend_count() {
        return send_count;
    }

    public void setSend_count(int send_count) {
        this.send_count = send_count;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getMessage_from() {
        return message_from;
    }

    public void setMessage_from(String message_from) {
        this.message_from = message_from;
    }

    public String getMessage_to() {
        return message_to;
    }

    public void setMessage_to(String message_to) {
        this.message_to = message_to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate_recieved() {
        return date_recieved;
    }

    public void setDate_recieved(String date_recieved) {
        this.date_recieved = date_recieved;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getRecieved_time() {
        return recieved_time;
    }

    public void setRecieved_time(long recieved_time) {
        this.recieved_time = recieved_time;
    }

}
