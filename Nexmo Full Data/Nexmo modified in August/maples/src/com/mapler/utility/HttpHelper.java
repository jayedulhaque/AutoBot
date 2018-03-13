package com.mapler.utility;

import com.mapler.model.SendHubModel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * @author nope
 */
public class HttpHelper {

    private static Logger log = Logger.getLogger(HttpHelper.class);

    public static String sendGet(String uri, String contentType) throws Exception {
        try {
            URL obj = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Content-Type", contentType);
            con.setRequestProperty("Accept-Charset", "UTF-8");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + uri);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        }
        return "";
    }

    public static String sendPost(String uri, String contentType, String content) throws Exception {
        try {
            URL obj = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            if (StringUtils.isNotBlank(contentType)) {
                conn.setRequestProperty("Content-Type", contentType);
            }

            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            wr.writeBytes(content);
            wr.flush();
            wr.close();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(content);

            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        }
        return "";
    }

    public static void main(String s[]) {
        try {
            String response = sendGet("https://api.sendhub.com/v1/inbox/?limit=100&unread=true&username=3234519546&api_key=d450239a022f1cab57005eecd222cb6bd3e5afaf", "application/json");
            JsonReader responseReader = Json.createReader(new StringReader(response));
            JsonObject responseAsObject = responseReader.readObject();
            JsonArray objectsJson = responseAsObject.getJsonArray("objects");
            if (objectsJson == null) {
                System.out.println("Return as object null here");
                return;
            }

            HashMap<String, SendHubModel> messages = new HashMap<String, SendHubModel>();
            for (JsonValue jsonValue : objectsJson) {
                JsonReader objectsReader = Json.createReader(new StringReader(jsonValue.toString()));
                JsonObject objectsAsObject = objectsReader.readObject();
                JsonArray contactsJson = objectsAsObject.getJsonArray("contacts");
                if (contactsJson == null) {
                    continue;
                }
                for (JsonValue contactsJsonValue : contactsJson) {
                    JsonReader contactsReader = Json.createReader(new StringReader(contactsJsonValue.toString()));
                    JsonObject contactsAsObject = contactsReader.readObject();
                    if (contactsAsObject.get("id_str") != null) {
                        String id_str = contactsAsObject.get("id_str").toString();
                        if (!messages.containsKey(s)) {
                            SendHubModel sendHubModel = new SendHubModel();
                            if (contactsAsObject.get("id") != null) {
                                sendHubModel.setId(contactsAsObject.get("id").toString());
                            }

                            if (contactsAsObject.get("number") != null) {
                                sendHubModel.setNumber(contactsAsObject.get("number").toString());
                            }
                            messages.put(id_str, sendHubModel);
                        }
                    }
                }
            }
            System.out.println(messages);
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        }
    }

    public static String post(URL url, String content) throws Exception {
        return sendReq(url, content, "POST");
    }

    public static String get(URL url) throws Exception {
        return sendReq(url, "", "GET");
    }

    public static String sendReq(URL url, String content, String reqType) throws Exception {
        HttpURLConnection conn = null;
        String result = "";
        try {
            conn = (HttpURLConnection) url.openConnection();
            if (reqType.equalsIgnoreCase("POST")) {
                writeData(content, "text/xml", conn, reqType);
            }
            result = read(conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private static void XwriteData(String content, String contentType, HttpURLConnection conn, String reqType) throws Exception {

        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(reqType);
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Content-Length", "" + content.length() + 3000);
        conn.setConnectTimeout(1 * 17 * 1000);
        conn.setReadTimeout(1 * 60 * 1000);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        try {
            writer.write(content);
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            writer.close();
        }
    }

    private static void writeData(String content, String contentType, HttpURLConnection conn, String reqType) throws Exception {
        DataOutputStream wr = null;
        try {
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(reqType);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", ""
                    + Integer.toString(content.getBytes().length + 7));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(1 * 17 * 1000);
            conn.setReadTimeout(1 * 60 * 1000);
            wr = new DataOutputStream(conn.getOutputStream());

            wr.writeBytes(content);
            wr.flush();
            wr.close();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(content);
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            if (wr != null) {
                wr.close();
            }
        }
    }

    public static String Xread(HttpURLConnection conn) throws IOException {
        conn.setConnectTimeout(1 * 17 * 1000);
        conn.setReadTimeout(1 * 60 * 1000);
        InputStream is = conn.getInputStream();
        Charset charset = Charset.forName("UTF8");
        InputStreamReader isr = new InputStreamReader(is, charset);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        return result;
    }

    public static String read(HttpURLConnection conn) throws IOException {
        try {
            //return IOUtils.toString(conn.getInputStream());
            conn.setConnectTimeout(1 * 17 * 1000);
            conn.setReadTimeout(1 * 60 * 1000);
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        }
        return "<Response><errorcode>000</errorcode><message>Request failed.</message></Response>";
    }
}
