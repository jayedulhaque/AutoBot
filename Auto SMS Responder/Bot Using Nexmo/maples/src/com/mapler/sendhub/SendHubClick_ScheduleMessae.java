package com.mapler.sendhub;

import com.mapler.model.SModel;
import com.mapler.model.SendHubModel;
import com.mapler.model.UAModel;
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
public class SendHubClick_ScheduleMessae implements Runnable {

    private SModel sModel;
    private static Logger log = Logger.getLogger(SendHubClick_ScheduleMessae.class);
    private INotifier iNotifier;
    private UAModel uAModel = new UAModel();
    MailSubjectReader mailSubjectReader;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
    List<SendHubModel> listOfMessage;
    int bundle = 19;
    ArrayList<String> ignoreNumbers = new ArrayList<String>();

    public SendHubClick_ScheduleMessae(INotifier iNotifier, SModel sModel) {
        this.sModel = sModel;
        this.iNotifier = iNotifier;
    }

    public SendHubClick_ScheduleMessae(SModel iModel) {
        this.sModel = iModel;
    }
    boolean firstClick = false;

    @Override
    public void run() {
        try {
            ignoreNumbers = readIgnoreile();
            firstClick = true;
            if (sModel.isVoice()) {
                log.debug("SendHub:Going to start working!!!");
                iNotifier.notify("SendHub:Going to start working!!!");
                while (true) {
                    doVoice();
                    firstClick = false;
                    if (!sModel.isRepeated() && sModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("SendHub:Wating for next job ...");
                    log.debug("SendHub:Wating for next job ...");
                    Util.wait(150);
                    iNotifier.notify("SendHub:Wait time up. Going to work agian...");
                    log.debug("SendHub:Wait time up. Going to work agian...");
                }
            } else {
                log.debug("SendHub:Going to start working!!!");
                iNotifier.notify("SendHub:Going to start working!!!");
                while (true) {
                    this.doOnSendHub();
                    if (!sModel.isRepeated() && sModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("SendHub:Wating for next job ...");
                    log.debug("SendHub:Wating for next job ...");
                    Util.wait(150);
                    iNotifier.notify("SendHub:Wait time up. Going to work agian...");
                    log.debug("SendHub:Wait time up. Going to work agian...");
                }
            }
            log.debug("SendHub:Run::Done !!!");
        } catch (Throwable ex) {
            iNotifier.notify("SendHub:Run:: stopped for error: " + ex.getMessage());
            log.error("SendHub:Run:: stopped causes " + ex);
        }
    }

    private void doVoice() {
        try {
            String fileName = "C:\\irobot\\sendhub\\vnumber.properties";
            iNotifier.notify("SendHub: Reading sended message number");
            HashMap<String, SendHubModel> vnumbers = this.readPropertiesFile(fileName);

            if (firstClick) {
                iNotifier.notify("SendHub: checking and marging new file for sending ");
                vnumbers = this.writeNewPropertieInPropsFile(vnumbers);
            }
            listOfMessage = readMessageFile(sModel);

            // Process new number
            if (firstClick) {
                iNotifier.notify("SendHub: Going to send first message");
                SendHubModel fModel = listOfMessage.get(0);
                sendFirstVoice(vnumbers, fModel);
                iNotifier.notify("SendHub: First message has been sended");
            }

            Util.wait(0);
            // Process next send after first send            
            sendRepeatedVoice(vnumbers, fileName, true);
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
        }
    }

    private void doOnSendHub() {
        Properties props = null;
        FileOutputStream out = null;
        try {
            String fileName = "C:\\irobot\\sendhub\\vnumber.properties";
            iNotifier.notify("SendHub: Reading sended message number");
            HashMap<String, SendHubModel> vnumbers = this.readPropertiesFile(fileName);
            listOfMessage = readMessageFile(sModel);
            sendRepeatedVoice(vnumbers, fileName, false);
        } catch (Exception ex) {
            iNotifier.notify("SendHub: Error on processing");
            ex.printStackTrace();
        } finally {
        }
    }

    private void sendRepeatedVoice(HashMap<String, SendHubModel> vnumbers, String fileName, boolean sendonvoice) {
        Properties props = null;
        FileOutputStream out = null;
        try {
            // Read propertied file
            copyFile();
            FileInputStream in = new FileInputStream(fileName);
            props = new Properties();
            props.load(in);
            in.close();

            // Open propertied file for add or update operations
            out = new FileOutputStream(fileName);

            iNotifier.notify("SendHub: Going to read inbox...");
            String response = HttpHelper.sendGet("https://api.sendhub.com/v1/inbox/?limit=" + sModel.getLimit() + "&unread=true&username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json");
            iNotifier.notify("SendHub: Read inbox has been done now waiting for executions");
            Util.wait(0);
            iNotifier.notify("SendHub: Wait time out now going for processing");
            JsonReader responseReader = Json.createReader(new StringReader(response));
            JsonObject responseAsObject = responseReader.readObject();
            JsonArray objectsJson = responseAsObject.getJsonArray("objects");
            if (objectsJson == null) {
                iNotifier.notify("SendHub: Return as object null here");
                return;
            }
            iNotifier.notify("SendHub: Read inbox done now on processing response");

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
                    if (sendonvoice && !vnumbers.containsKey(number)) {
                        continue;
                    }

                    if (contactsAsObject.get("id_str") == null) {
                        continue;
                    }
                    String id_str = contactsAsObject.get("id_str").toString().replaceAll("\"", "");

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

                    String message = props.getProperty(number);
                    if (sendonvoice && StringUtils.isBlank(message)) {
                        continue;
                    }

                    String[] msgs = {};
                    if (message != null) {
                        msgs = message.split("@-000-@");
                    }

                    Long sendedcount = new Long(0);
                    long lastSent = 0;
                    // If sendonvoice then must check otherwise dont need if not message is there...
                    if (sendonvoice || (msgs != null && msgs.length > 1)) {
                        sendedcount = Long.parseLong(msgs[0]);
                        lastSent = Long.parseLong(msgs[1]);
                    }

                    // If the message sended count is greater then the message list count then ignore that
                    if (sendedcount > listOfMessage.size()) {
                        continue;
                    }
                    String dateInString = objectsAsObject.get("sent").toString();

                    dateInString = dateInString.replace("T", " ");
                    dateInString = dateInString.substring(0, dateInString.indexOf(".")).replaceAll("\"", "");

                    Date date = formatter.parse(dateInString);
                    long jsonLastSent = date.getTime() / 1000;

                    System.out.println(dateInString + " : " + jsonLastSent);

                    if (jsonLastSent == lastSent) {
                        continue;
                    }

                    Long nextCount = sendedcount + 1;
                    props.setProperty(number, nextCount + "@-000-@" + jsonLastSent + "@-000-@" + text);

                    iNotifier.notify("SendHub: going to send them message");
                    SendHubModel shm = listOfMessage.get(nextCount.intValue() - 1);
                    String newDateInString = this.prepareScheduleDate(dateInString, shm.getWaitTimeInSec() * 1000);
                    String messageWillBeSend = shm.getMessage();
                    // Getting value from list need to minus 1 as index starts from 0
                    sendVoiceMessage(number, messageWillBeSend, newDateInString);
                    iNotifier.notify("SendHub: counted " + bundle + " Message sent done...");
                }
            }
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
        for (SendHubModel m : listOfMessage) {
            if (StringUtils.equals(m.getMessage().trim(), text.trim())) {
                return true;
            }
        }
        return false;
    }

    private void sendFirstVoice(HashMap<String, SendHubModel> vnumbers, SendHubModel fModel) {
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

            iNotifier.notify("SendHub: Preparing first message and sending...");
            for (String key : vnumbers.keySet()) {
                if (StringUtils.isBlank(key)) {
                    continue;
                }

                if (ignoreNumbers.contains(key)) {
                    continue;
                }
                String message = props.getProperty(key);
                if (StringUtils.isNotBlank(message)) {
                    String[] msgs = message.split("@-000-@");
                    if (Long.parseLong(msgs[0]) > 0) {
                        continue;
                    }
                }
                props.setProperty(key, "1@-000-@" + System.currentTimeMillis() / 1000 + "@-000-@I");
                sendVoiceMessage(key, fModel.getMessage(), getUTCTime(fModel.getWaitTimeInSec() * 1000));
            }
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

    private void sendVoiceMessage(String key, String text, String date) {
        //{ "contacts": [1111],"text": "Testing"}
        try {
            iNotifier.notify("SendHub: on sending to sendhub");
            StringBuilder jsonData = new StringBuilder();
            jsonData.append(IConstant.BRACE_SECOND_START);
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("contacts");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append(IConstant.BRACE_THIRD_START);
            jsonData.append("\"").append(key).append("\"");
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

            jsonData.append(IConstant.COMMA);
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("scheduled_at");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(date);
            jsonData.append(IConstant.DOUBLE_QUOTE);


            jsonData.append(IConstant.BRACE_SECOND_END);

            //String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=3234519546&api_key=d450239a022f1cab57005eecd222cb6bd3e5afaf", "application/json", jsonData.toString());
            String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json", jsonData.toString());
            log.debug("Request json : " + jsonData.toString());
            log.debug("Response json : " + response);

            iNotifier.notify("SendHub: on sending to sendhub done");
        } catch (Throwable ex) {
            log.error("SendHub:Run:: stopped causes " + ex);
            ex.printStackTrace();
        }
    }

    public void sendMessage(HashMap<String, SendHubModel> messages, SendHubModel sm) {
        //{ "contacts": [1111],"text": "Testing"}
        try {
            StringBuilder jsonData = new StringBuilder();
            jsonData.append(IConstant.BRACE_SECOND_START);
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("contacts");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append(IConstant.BRACE_THIRD_START);
            boolean needComma = false;
            for (String key : messages.keySet()) {
                if (needComma) {
                    jsonData.append(",");
                }
                needComma = true;
                SendHubModel sendHubModel = messages.get(key);
                jsonData.append(sendHubModel.getId_str());
            }
            jsonData.append(IConstant.BRACE_THIRD_END);
            jsonData.append(IConstant.COMMA);

            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("text");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append(IConstant.DOUBLE_QUOTE);
            String message = StringEscapeUtils.escapeJson(sm.getMessage());
            jsonData.append(message);
            jsonData.append(IConstant.DOUBLE_QUOTE);

            jsonData.append(IConstant.BRACE_SECOND_END);

            //String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=3234519546&api_key=d450239a022f1cab57005eecd222cb6bd3e5afaf", "application/json", jsonData.toString());
            String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json", jsonData.toString());

        } catch (Exception ex) {
            log.error("SendHub:Run:: stopped causes " + ex);
            ex.printStackTrace();
        }
    }

    public ArrayList<String> readVoiceFile() throws Exception {
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
            throw new Exception(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return numbers;
    }

    public ArrayList<String> readIgnoreile() throws Exception {
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
            throw new Exception(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return numbers;
    }

    // Splitted by '@-000-@'
    public HashMap<String, SendHubModel> writeNewPropertieInPropsFile(HashMap<String, SendHubModel> models) throws Exception {
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
                    if (existingKeys.contains(sCurrentLine)) {
                        continue;
                    }
                    SendHubModel sm = new SendHubModel();
                    props.setProperty(sCurrentLine, "0@-000-@0@-000-@I");
                    sm.setSentCount(0);
                    sm.setLastSentTimeInSec(0);
                    models.put(sCurrentLine, sm);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            iNotifier.notify("SendHub:readfile Stopped for error: " + e.getMessage());
            log.error("SendHub:doPost::readfile Stopped causes..." + e);
            throw new Exception(e);
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

    public HashMap<String, SendHubModel> readPropertiesFile(String fileName) throws Exception {
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
            throw new Exception(e);
        } finally {
        }
        return models;
    }

    public ArrayList<String> readSendedFile() throws Exception {
        ArrayList<String> numbers = new ArrayList<String>();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            //URL uri = getClass().getResource("/com/mapler/resources/jobs_city.txt");
            String uri = "C:\\irobot\\sendhub\\sended.csv";
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
            throw new Exception(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return numbers;
    }

    public void writeNumber(HashMap<String, SendHubModel> messages) {
        BufferedWriter output = null;
        try {
            iNotifier.notify("Writing in file...");
            File file = new File("C:\\irobot\\sendhub\\sended.csv");
            output = new BufferedWriter(new FileWriter(file, true));

            for (String key : messages.keySet()) {
                SendHubModel sendHubModel = messages.get(key);
                output.newLine();
                output.write(sendHubModel.getNumber().replaceAll("\"", ""));

            }
            iNotifier.notify("Writing into file done");
        } catch (Throwable ex) {
            ex.printStackTrace();
            iNotifier.notify("SendHub:Run:: stopped for error: " + ex.getMessage());
            log.error("SendHub:Run:: stopped causes " + ex);
        } finally {
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException ex) {
                    iNotifier.notify("SendHub:Run:: stopped for error: " + ex.getMessage());
                    log.error("SendHub:Run:: stopped causes " + ex);
                }
            }
        }
    }

    public void makeUnread(HashMap<String, SendHubModel> messages, SendHubModel sm) {
        //{ "contacts": [1111],"text": "Testing"}
        try {
            StringBuilder jsonData = new StringBuilder();
            jsonData.append(IConstant.BRACE_SECOND_START);
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append("unread");
            jsonData.append(IConstant.DOUBLE_QUOTE);
            jsonData.append(IConstant.COLON);

            jsonData.append("false");
            jsonData.append(IConstant.BRACE_SECOND_END);

            //String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=3234519546&api_key=d450239a022f1cab57005eecd222cb6bd3e5afaf", "application/json", jsonData.toString());
            String response = HttpHelper.sendPost("https://api.sendhub.com/v1/messages/?username=" + sModel.getEmail() + "&api_key=" + sModel.getEmailPassword(), "application/json", jsonData.toString());

        } catch (Exception ex) {
            log.error("SendHub:Run:: stopped causes " + ex);
            ex.printStackTrace();
        }
    }

    public List<SendHubModel> readMessageFile(SModel m) {
        try {
            List<SendHubModel> sendHubModels = new ArrayList<SendHubModel>();
            FileInputStream file = new FileInputStream("C:\\irobot\\sendhub\\message.xls");
            if (StringUtils.isNotBlank(m.getFfPath())) {
                file = new FileInputStream(m.getFfPath());
            }

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isHeader = true;
            while (rowIterator.hasNext()) {
                if (isHeader) {
                    isHeader = false;
                    rowIterator.next();
                    continue;
                }

                SendHubModel sendHubModel = new SendHubModel();
                Row row = rowIterator.next();
                if (row.getCell(0) != null) {
                    sendHubModel.setMessage(row.getCell(0).toString().trim());
                } else {
                    sendHubModel.setMessage("");
                }

                if (row.getCell(1) != null) {
                    BigDecimal d = new BigDecimal(row.getCell(1).toString().trim());
                    sendHubModel.setWaitTimeInSec(d.intValue());
                } else {
                    sendHubModel.setWaitTimeInSec(0);
                }
                sendHubModels.add(sendHubModel);
            }
            return sendHubModels;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String prepareScheduleDate(String dateInString, long inMiliSec) {
        String newtime = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date date = formatter.parse(dateInString);
            long time = date.getTime() + inMiliSec;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            Date date1 = calendar.getTime();
            newtime = formatter.format(date1);
            newtime = newtime.replace(" ", "T") + "-0800";

            System.out.println("sss ::: " + newtime);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return newtime;
    }

    private String getUTCTime(long inMiliSec) {
        String utcTime = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            long time = System.currentTimeMillis() + inMiliSec;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            Date date1 = calendar.getTime();


            utcTime = formatter.format(date1);
            utcTime = utcTime.replace(" ", "T") + "-0800";

            System.out.println("sss ::: " + utcTime);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return utcTime;
    }

    public static void main(String s[]) {
        try {
            //SendHubClick sendHubClick = new SendHubClick(null);
            //HashMap<String, SendHubModel> messages = sendHubClick.readPropertiesFile();
            //messages = sendHubClick.writeNewPropertieInPropsFile(messages);

            /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String utcTime = formatter.format(new Date());

            if (true) {
                System.out.println(utcTime);
                return;
            }
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
            System.out.println(date.getTime() / 1000);
            
             dateInString = "2015-02-25 14:45:11";
             date = formatter.parse(dateInString);
             System.out.println(date.getTime() / 1000);
             */
            
            SendHubClick_ScheduleMessae sendHubClick = new SendHubClick_ScheduleMessae(null);
            String utcTime = sendHubClick.getUTCTime(20 * 1000);

            if (true) {
                System.out.println(utcTime);
                return;
            }
            
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
