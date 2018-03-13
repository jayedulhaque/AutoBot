package com.mapler.sendhub;

import com.mapler.model.SModel;
import com.mapler.model.SendHubModel;
import com.mapler.service.INotifier;
import com.mapler.utility.HttpHelper;
import com.mapler.utility.IConstant;
import com.mapler.utility.MailSubjectReader;
import com.mapler.utility.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author none
 */
public class SendHubClick implements Runnable {

    private SModel sModel;
    private static Logger log = Logger.getLogger(SendHubClick.class);
    private INotifier iNotifier;
    MailSubjectReader mailSubjectReader;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
    List<List<SendHubModel>> listOfMessage;
    int bundle = 19;
    ArrayList<String> ignoreNumbers = new ArrayList<String>();
    private Random random;

    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public SendHubClick(INotifier iNotifier, SModel sModel) {
        this.sModel = sModel;
        this.iNotifier = iNotifier;
    }

    public SendHubClick(SModel iModel) {
        this.sModel = iModel;
    }
    boolean firstClick = false;

    @Override
    public void run() {
        try {
            bundle = sModel.getSendHubSendLimit();
            ignoreNumbers = readIgnoreFile();
            firstClick = true;
            if (sModel.isVoice()) {
                log.debug("SendHub:Going to start working!!!");
                iNotifier.notify("SendHub:Going to start working!!!");
                while (true) {
                    doVoice();
                    firstClick = false;
                    if (sModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("SendHub:Wating for next job ...");
                    log.debug("SendHub:Wating for next job ...");

                    int timeTOWait = random.nextInt(sModel.getSendHubRepeatTime().length);
                    timeTOWait = Integer.parseInt(sModel.getSendHubRepeatTime()[timeTOWait].trim());
                    Util.wait(timeTOWait);
                    iNotifier.notify("SendHub:Wait time up. Going to work agian...");
                    log.debug("SendHub:Wait time up. Going to work agian...");
                    if (!sModel.isRepeated()) {
                        break;
                    }
                }
            } else {
                log.debug("SendHub:Going to start working!!!");
                iNotifier.notify("SendHub:Going to start working!!!");
                while (true) {
                    this.doOnSendHub();
                    if (sModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("SendHub:Wating for next job ...");
                    log.debug("SendHub:Wating for next job ...");
                    int timeTOWait = random.nextInt(sModel.getSendHubRepeatTime().length);
                    timeTOWait = Integer.parseInt(sModel.getSendHubRepeatTime()[timeTOWait].trim());
                    Util.wait(timeTOWait);
                    iNotifier.notify("SendHub:Wait time up. Going to work agian...");
                    log.debug("SendHub:Wait time up. Going to work agian...");
                    if (!sModel.isRepeated()) {
                        break;
                    }
                }
            }
            iNotifier.notify("SendHub:Run:: Has been stopped working.");
        } catch (Throwable ex) {
            iNotifier.notify("SendHub:Run:: stopped for error: " + ex.getMessage());
            log.error("SendHub:Run:: stopped causes " + ex);
        }
    }

    private void doVoice() {
        try {
            iNotifier.notify("SendHub: checking and marging new file for sending ");
            HashMap<String, SendHubModel> vnumbers = this.readVNumAndWriteNewPropertieInPropsFile();

            listOfMessage = readMessageFile(sModel);

            // Process new number
            if (firstClick) {
                iNotifier.notify("SendHub: Going to send first message");
                int next = random.nextInt(listOfMessage.get(0).size());
                SendHubModel fModel = listOfMessage.get(0).get(next);
                sendFirstVoice(vnumbers, fModel);
                iNotifier.notify("SendHub: First message has been sent");
            }

            // Process next send after first send            
            sendRepeatedVoice(vnumbers, true);
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
        }
    }

    private void doOnSendHub() {
        try {
            String fileName = "C:\\irobot\\sendhub\\vnumber.properties";
            iNotifier.notify("SendHub: Reading sended message number");
            HashMap<String, SendHubModel> vnumbers = this.readPropertiesFile(fileName);
            listOfMessage = readMessageFile(sModel);
            sendRepeatedVoice(vnumbers, false);
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
        }
    }

    private void sendFirstVoice(HashMap<String, SendHubModel> vnumbers, SendHubModel fModel) {
        try {
            iNotifier.notify("SendHub: Preparing first message and sending...");
            int count = 0;
            List<String> numListToBeSend = new ArrayList<String>();
            for (String key : vnumbers.keySet()) {
                if (StringUtils.isBlank(key)) {
                    continue;
                }

                SendHubModel shm = vnumbers.get(key);
                if (shm.getSentCount() > 0) {
                    continue;
                }

                if (ignoreNumbers.contains(key)) {
                    continue;
                }
                count = count + 1;
                numListToBeSend.add(key);
                if (count == bundle) {
                    ArrayList<SendHubResponse> hubResponses = sendVoiceMessage(numListToBeSend, fModel.getMessage());
                    this.updatePropertieInPropsFile(hubResponses, 1);
                    numListToBeSend.clear();
                    count = 0;
                }
            }
            if (!numListToBeSend.isEmpty() && count < bundle) {
                ArrayList<SendHubResponse> hubResponses = sendVoiceMessage(numListToBeSend, fModel.getMessage());
                this.updatePropertieInPropsFile(hubResponses, 1);
                numListToBeSend.clear();
            }
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
        }
    }

    private void sendRepeatedVoice(HashMap<String, SendHubModel> fileVNumbers, boolean sendonvoice) {
        try {
            iNotifier.notify("SendHub: Going to read inbox...");
            String response = HttpHelper.sendGet("https://api.sendhub.com/v1/inbox/?limit=" + sModel.getLimit() + "&unread=true&username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json");
            iNotifier.notify("SendHub: Read inbox has been done now waiting for executions");

            int timeTOWait = random.nextInt(sModel.getSendHubWaiTimeBeforeSend().length);
            timeTOWait = Integer.parseInt(sModel.getSendHubWaiTimeBeforeSend()[timeTOWait].trim());
            Util.wait(timeTOWait);

            iNotifier.notify("SendHub: Wait time out now going for processing");
            JsonReader responseReader = Json.createReader(new StringReader(response));
            JsonObject responseAsObject = responseReader.readObject();
            JsonArray objectsJson = responseAsObject.getJsonArray("objects");
            if (objectsJson == null) {
                iNotifier.notify("SendHub: Return as object null here");
                return;
            }
            iNotifier.notify("SendHub: Read inbox done now on processing response");

            HashMap<Long, ArrayList<String>> numberToBeSend = new HashMap<Long, ArrayList<String>>();
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

                    if (contactsAsObject.get("number") == null) {
                        continue;
                    }

                    if (objectsAsObject.get("type") == null) {
                        continue;
                    }

                    String type = objectsAsObject.get("type").toString().replaceAll("\"", "");
                    if (!StringUtils.equalsIgnoreCase(type, "message-in")) {
                        continue;
                    }

                    if (objectsAsObject.get("unread") == null) {
                        continue;
                    }
                    boolean unread = objectsAsObject.getBoolean("unread");
                    if (!unread) {
                        continue;
                    }

                    String number = contactsAsObject.get("number").toString().replaceAll("\"", "");

                    if (ignoreNumbers.contains(number)) {
                        continue;
                    }

                    // If the number is not a sended number then ignore that
                    if (sendonvoice && !fileVNumbers.containsKey(number)) {
                        continue;
                    }

                    // If 0 meant first number then just return from here as first message already sent initally
                    SendHubModel shm = fileVNumbers.get(number);

                    if (sendonvoice) {
                        if (shm.getSentCount() == 0) {
                            continue;
                        }
                    }

                    // If null its meant this is fresh send hub number
                    if (shm == null) {
                        shm.setSentCount(0);
                        shm.setLastSentTimeInSec(0);
                    }

                    if (objectsAsObject.get("sent") == null) {
                        continue;
                    }
                    if (objectsAsObject.get("text") == null) {
                        continue;
                    }
                    String text = objectsAsObject.get("text").toString();
                    if (isSlefMessage(text)) {
                        continue;
                    }

                    // Check the eairlier message is deliverd or not
                    SendHubResponse statusResponse = this.checkMessageStatus(shm.getMessageId());
                    // If the time is greater than old store time for request message. Its meant the message has been delivered
                    if (statusResponse.getSentTime() < shm.getLastSentTimeInSec()) {
                        continue;
                    }

                    // Now check inbox message time is greater than delivery time if okay then send sms otherwise move back
                    String dateInString = objectsAsObject.get("sent").toString();

                    dateInString = dateInString.replace("T", " ");
                    dateInString = dateInString.substring(0, dateInString.indexOf(".")).replaceAll("\"", "");

                    Date date = formatter.parse(dateInString);
                    long jsonLastSent = date.getTime() / 1000;
                    if (statusResponse.getSentTime() > jsonLastSent) {
                        continue;
                    }

                    long sendedcount = shm.getSentCount();
                    Long nextCount = sendedcount + 1;

                    // if next count greater than list of message size then return from here.
                    if (nextCount > listOfMessage.size()) {
                        continue;
                    }
                    if (!numberToBeSend.containsKey(nextCount)) {
                        numberToBeSend.put(nextCount, new ArrayList<String>());
                    }
                    numberToBeSend.get(nextCount).add(number);

                    if (numberToBeSend.get(nextCount).size() == bundle) {
                        iNotifier.notify("SendHub: counted " + bundle + " going to send them message");

                        // Getting value from list need to minus 1 as index starts from 0
                        int next = random.nextInt(listOfMessage.get(nextCount.intValue() - 1).size());
                        SendHubModel fModel = listOfMessage.get(nextCount.intValue() - 1).get(next);
                        ArrayList<SendHubResponse> hubResponses = sendVoiceMessage(numberToBeSend.get(nextCount), fModel.getMessage());
                        this.updatePropertieInPropsFile(hubResponses, nextCount);
                        numberToBeSend.get(nextCount).clear();
                        iNotifier.notify("SendHub: counted " + bundle + " Message sent done...");
                    }
                }
            }

            for (Long c : numberToBeSend.keySet()) {
                if (numberToBeSend.get(c).size() > 0) {
                    iNotifier.notify("SendHub: counted end going to send them message");
                    int next = random.nextInt(listOfMessage.get(c.intValue() - 1).size());
                    SendHubModel fModel = listOfMessage.get(c.intValue() - 1).get(next);
                    ArrayList<SendHubResponse> hubResponses = sendVoiceMessage(numberToBeSend.get(c), fModel.getMessage());
                    this.updatePropertieInPropsFile(hubResponses, c.intValue());
                    numberToBeSend.get(c).clear();
                    iNotifier.notify("SendHub: counted end going to send them message");
                }
            }
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
        }
    }

    private ArrayList<SendHubResponse> parseResponse(String response) {
        iNotifier.notify("SendHub: parsing response");
        ArrayList<SendHubResponse> sendedResponse = new ArrayList<SendHubResponse>();
        try {
            JsonReader responseReader = Json.createReader(new StringReader(response));
            JsonObject responseAsObject = responseReader.readObject();

            if (responseAsObject.get("sent") == null) {
                return sendedResponse;
            }
            if (responseAsObject.get("text") == null) {
                return sendedResponse;
            }

            String messageId = responseAsObject.get("message_id").toString();
            if (messageId == null) {
                return sendedResponse;
            }
            messageId = messageId.replaceAll("\"", "");

            String text = responseAsObject.get("text").toString();

            String dateInString = responseAsObject.get("sent").toString();

            dateInString = dateInString.replace("T", " ");
            dateInString = dateInString.substring(0, dateInString.indexOf(".")).replaceAll("\"", "");

            Date date = formatter.parse(dateInString);
            long jsonLastSent = date.getTime() / 1000;

            iNotifier.notify("SendHub: Read inbox done now on processing response");

            JsonArray contactsJson = responseAsObject.getJsonArray("contacts");
            if (contactsJson == null) {
                return sendedResponse;
            }
            for (JsonValue contactsJsonValue : contactsJson) {
                JsonReader contactsReader = Json.createReader(new StringReader(contactsJsonValue.toString()));
                JsonObject contactsAsObject = contactsReader.readObject();

                if (contactsAsObject.get("number") == null) {
                    continue;
                }

                String number = contactsAsObject.get("number").toString().replaceAll("\"", "");

                if (ignoreNumbers.contains(number)) {
                    continue;
                }
                SendHubResponse hubResponse = new SendHubResponse();
                hubResponse.setText(text);
                hubResponse.setSentTime(jsonLastSent);
                hubResponse.setNumber(number);
                hubResponse.setMessageId(messageId);
                sendedResponse.add(hubResponse);
            }

        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on response processing");
            ex.printStackTrace();
        }
        iNotifier.notify("SendHub: parsing response. Done.");
        return sendedResponse;
    }

    public SendHubResponse checkMessageStatus(String messageId) {
        try {
            iNotifier.notify("SendHub: Going to check message status");
            //String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=3234519546&api_key=d450239a022f1cab57005eecd222cb6bd3e5afaf", "application/json", jsonData.toString());
            String response = HttpHelper.sendGet("https://api.sendhub.com/v1/messages/" + messageId + "/?username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json");
            ArrayList<SendHubResponse> sendedResponse = parseResponse(response);

            if (sendedResponse != null && !sendedResponse.isEmpty()) {
                if (sendedResponse.get(0).getMessageId().equals(messageId)) {
                    return sendedResponse.get(0);
                }
            }
            iNotifier.notify("SendHub: Message status check done");
        } catch (Throwable ex) {
            log.error("SendHub:Run:: stopped causes " + ex);
            ex.printStackTrace();
        }
        return null;
    }

    public void copyFile() {
        try {
            Path FROM = Paths.get("C:\\irobot\\sendhub\\vnumber.properties");
            Path TO = Paths.get("C:\\irobot\\sendhub\\vnumber.properties.inb");
            //overwrite existing file, if exists
            CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
            };
            Files.copy(FROM, TO, options);
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        }
    }

    private boolean isSlefMessage(String text) {
        for (List<SendHubModel> upMessage : listOfMessage) {
            for (SendHubModel m : upMessage) {
                if (StringUtils.equals(m.getMessage().trim(), text.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<SendHubResponse> sendVoiceMessage(List<String> messages, String text) {
        //{ "contacts": [1111],"text": "Testing"}
        ArrayList<SendHubResponse> sendedResponse = new ArrayList<SendHubResponse>();
        try {
            iNotifier.notify("SendHub: on sending to sendhub first message");
            StringBuilder jsonData = new StringBuilder();
            jsonData.append(IConstant.BRACE_SECOND_START);
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("contacts");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append(IConstant.BRACE_THIRD_START);
            boolean needComma = false;
            for (String key : messages) {
                if (needComma) {
                    jsonData.append(",");
                }
                needComma = true;
                jsonData.append("\"").append(key).append("\"");
            }
            jsonData.append(IConstant.BRACE_THIRD_END);
            jsonData.append(IConstant.COMMA);

            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("text");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append(IConstant.DOUBLE_QUOTE);

            String message = StringEscapeUtils.escapeJson(text);
            jsonData.append(message);
            jsonData.append(IConstant.DOUBLE_QUOTE);

            jsonData.append(IConstant.BRACE_SECOND_END);

            //String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=3234519546&api_key=d450239a022f1cab57005eecd222cb6bd3e5afaf", "application/json", jsonData.toString());
            String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json", jsonData.toString());
            log.debug("Request json : " + jsonData.toString());
            log.debug("Response json : " + response);

            sendedResponse = parseResponse(response);

            iNotifier.notify("SendHub: on sending to sendhub first message done");
        } catch (Throwable ex) {
            log.error("SendHub:Run:: stopped causes " + ex);
            ex.printStackTrace();
        }

        return sendedResponse;
    }

    public ArrayList<String> readVoiceFile() {
        iNotifier.notify("SendHub:Reading voice file. Dont exit program now.");
        ArrayList<String> numbers = new ArrayList<String>();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            //URL uri = getClass().getResource("/com/mapler/resources/jobs_city.txt");
            String uri = "C:\\irobot\\sendhub\\voicenumber.csv";
            br = new BufferedReader(new FileReader(uri));
            while ((sCurrentLine = br.readLine()) != null) {
                if (StringUtils.isNotBlank(sCurrentLine)) {
                    numbers.add(sCurrentLine.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            iNotifier.notify("SendHub:readfile Stopped for error: " + e.getMessage());
            log.error("SendHub:doPost::readfile Stopped causes..." + e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        iNotifier.notify("SendHub:Reading voice file. Done.");
        return numbers;
    }

    public ArrayList<String> readIgnoreFile() {
        iNotifier.notify("SendHub:Reading ignore file.");
        ArrayList<String> numbers = new ArrayList<String>();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            //URL uri = getClass().getResource("/com/mapler/resources/jobs_city.txt");
            String uri = "C:\\irobot\\sendhub\\ignorenumber.csv";
            br = new BufferedReader(new FileReader(uri));
            while ((sCurrentLine = br.readLine()) != null) {
                if (StringUtils.isNotBlank(sCurrentLine)) {
                    numbers.add(sCurrentLine.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            iNotifier.notify("SendHub:readfile Stopped for error: " + e.getMessage());
            log.error("SendHub:doPost::readfile Stopped causes..." + e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        iNotifier.notify("SendHub:Reading ignore file. Done");
        return numbers;
    }

    private void updatePropertieInPropsFile(ArrayList<SendHubResponse> responses, long sendedCount) {
        iNotifier.notify("SendHub: Updating property file. Dont exit the program now!!!");
        Properties props = null;
        FileOutputStream out = null;
        try {
            // Read propertied file
            copyFile();
            FileInputStream in = new FileInputStream("C:\\irobot\\sendhub\\vnumber.properties");
            props = new Properties();
            props.load(in);
            in.close();

            // Open propertied file for add or update operations
            out = new FileOutputStream("C:\\irobot\\sendhub\\vnumber.properties");
            for (SendHubResponse response : responses) {
                String message = props.getProperty(response.getNumber());
                if (StringUtils.isNotBlank(message)) {
                    props.setProperty(response.getNumber(), sendedCount + "@-000-@" + response.getSentTime() + "@-000-@" + response.getText() + "@-000-@" + response.getMessageId());
                }
            }
            iNotifier.notify("SendHub: Updating property file. Done!!!");
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
            try {
                if (props != null) {
                    props.store(out, null);
                }

                if (out != null) {
                    out.close();
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    // Splitted by '@-000-@'
    public HashMap<String, SendHubModel> readVNumAndWriteNewPropertieInPropsFile() {
        iNotifier.notify("SendHub: Reading and writing property file. Dont exit the program now!!!");
        HashMap<String, SendHubModel> models = new HashMap<String, SendHubModel>();
        BufferedReader br = null;
        Properties props = null;
        FileOutputStream out = null;
        try {
            String sCurrentLine;
            //URL uri = getClass().getResource("/com/mapler/resources/jobs_city.txt");
            String uri = "C:\\irobot\\sendhub\\voicenumber.csv";
            copyFile();
            FileInputStream in = new FileInputStream("C:\\irobot\\sendhub\\vnumber.properties");
            props = new Properties();
            props.load(in);
            in.close();

            out = new FileOutputStream("C:\\irobot\\sendhub\\vnumber.properties");
            br = new BufferedReader(new FileReader(uri));
            Set<String> existingKeys = props.stringPropertyNames();
            while ((sCurrentLine = br.readLine()) != null) {
                if (StringUtils.isNotBlank(sCurrentLine)) {
                    SendHubModel sm = new SendHubModel();
                    //count, sent time, message, message messageId                   
                    if (existingKeys.contains(sCurrentLine)) {
                        String message = props.getProperty(sCurrentLine);
                        if (StringUtils.isBlank(message)) {
                            continue;
                        }
                        String[] msgs = {};
                        if (message != null) {
                            msgs = message.split("@-000-@");
                        }
                        Long sendedcount = Long.parseLong(msgs[0]);
                        long lastSent = Long.parseLong(msgs[1]);

                        String lastMessage = msgs[2];
                        String messageId = msgs[3];
                        sm.setSentCount(sendedcount);
                        sm.setLastSentTimeInSec(lastSent);
                        sm.setMessage(lastMessage);
                        sm.setMessageId(messageId);
                    } else {
                        props.setProperty(sCurrentLine, "0@-000-@0@-000-@I@-000-@I");
                        sm.setSentCount(0);
                        sm.setLastSentTimeInSec(0);
                    }
                    models.put(sCurrentLine, sm);
                }
            }
            iNotifier.notify("SendHub: Reading and writing property file. Done!!!");
        } catch (IOException e) {
            e.printStackTrace();
            iNotifier.notify("SendHub:readfile Stopped for error: " + e.getMessage());
            log.error("SendHub:doPost::readfile Stopped causes..." + e);
        } finally {
            try {
                if (props != null) {
                    props.store(out, null);
                }

                if (out != null) {
                    out.close();
                }

                if (br != null) {
                    br.close();
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return models;
    }

    public HashMap<String, SendHubModel> readPropertiesFile(String fileName) {
        iNotifier.notify("SendHub: Reading property file. Dont exit program now");
        HashMap<String, SendHubModel> models = new HashMap<String, SendHubModel>();
        try {
            FileInputStream in = new FileInputStream(fileName);
            Properties props = new Properties();
            props.load(in);
            for (String key : props.stringPropertyNames()) {
                SendHubModel m = new SendHubModel();
                m.setNumber(key);
                String message = props.getProperty(key);
                if (StringUtils.isNotBlank(message)) {
                    String[] msgs = message.split("@-000-@");
                    m.setSentCount(Long.parseLong(msgs[0]));
                    m.setLastSentTimeInSec(Long.parseLong(msgs[1]));
                    if (StringUtils.equals(msgs[2], "I")) {
                        m.setMessage("");
                    } else {
                        m.setMessage(msgs[2]);
                    }
                }
                models.put(key, m);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            iNotifier.notify("SendHub:readfile Stopped for error: " + e.getMessage());
            log.error("SendHub:doPost::readfile Stopped causes..." + e);
        } finally {
        }
        iNotifier.notify("SendHub: Reading property file. Done");
        return models;
    }

    public List<List<SendHubModel>> readMessageFile(SModel m) {
        try {
            iNotifier.notify("SendHub: Reading Message file");
            List<List<SendHubModel>> sendHubModels = new ArrayList<List<SendHubModel>>();
            FileInputStream file = new FileInputStream("C:\\irobot\\sendhub\\message.xls");
            if (StringUtils.isNotBlank(m.getFfPath())) {
                file = new FileInputStream(m.getFfPath());
            }

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isHeader = true;
            while (rowIterator.hasNext()) {
                List<SendHubModel> model = new ArrayList<SendHubModel>();
                if (isHeader) {
                    isHeader = false;
                    rowIterator.next();
                    continue;
                }
                Row row = rowIterator.next();

                BigDecimal d = new BigDecimal(0.0);
                if (row.getCell(1) != null) {
                    d = new BigDecimal(row.getCell(1).toString().trim());
                }
                if (row.getCell(0) != null) {
                    String msgs = row.getCell(0).toString().trim();
                    String[] msg = msgs.split("@@@");
                    for (String s : msg) {
                        SendHubModel sendHubModel = new SendHubModel();
                        sendHubModel.setMessage(s);
                        model.add(sendHubModel);
                        sendHubModel.setWaitTimeInSec(d.intValue());
                    }
                }
                sendHubModels.add(model);
            }
            iNotifier.notify("SendHub: Reading Message file. Done");
            return sendHubModels;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String s[]) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String utcTime = formatter.format(new Date());

            String[] sendHubRepeatTime = {"1", "2"};
            System.out.println(sendHubRepeatTime.length);

            if (true) {
                System.out.println(utcTime);
                return;
            }

            //SendHubClick sendHubClick = new SendHubClick(null);
            //HashMap<String, SendHubModel> messages = sendHubClick.readPropertiesFile();
            //messages = sendHubClick.writeNewPropertieInPropsFile(messages);

            //String dateInString = "2015-02-25 14:23:24";
            String dateInString = "2015-02-27T03:45:57.204704";
            dateInString = dateInString.replace("T", " ");
            dateInString = dateInString.substring(0, dateInString.indexOf("."));

            Date date = formatter.parse(dateInString);
            long time = date.getTime() + 3 * 1000;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            Date date1 = calendar.getTime();
            String sss = formatter.format(date1);
            sss = sss.replace(" ", "T") + "-0800";

            System.out.println("sss ::: " + sss);
            /*System.out.println(date.getTime() / 1000);
            
             dateInString = "2015-02-25 14:45:11";
             date = formatter.parse(dateInString);
             System.out.println(date.getTime() / 1000);
             */
            if (true) {
                return;
            }

            Pattern p = Pattern.compile("\\(\\d{3}\\)\\s\\d{3}-\\d{4}");
            //String mm = "(804) 298-8353";
            String subject = "SMS from (412) 874-0718";
            Matcher matcher = p.matcher(subject);
            //subject = StringUtils.substringBetween(subject, "\\(\\d{3}\\)\\s\\d{3}-\\d{4}");  

            while (matcher.find()) {
                System.out.println("Match: " + matcher.group());
                return;
            }

            File file = new File("c:\\irobot\\scrapper.csv");
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.write("\n");
            output.write("mithun");
            output.close();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
