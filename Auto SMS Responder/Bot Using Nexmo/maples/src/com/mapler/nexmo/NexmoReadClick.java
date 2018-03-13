package com.mapler.nexmo;

import com.mapler.service.INotifier;
import com.mapler.utility.HttpHelper;
import com.mapler.utility.Util;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.net.Urls;

/**
 *
 * @author JAYED
 */
public class NexmoReadClick implements Runnable {

    private NexmoModel nexmoModel;
    private static Logger log = Logger.getLogger(NexmoReadClick.class);
    private INotifier iNotifier;
    private Random random;
    private DBUtil dbutil;
    List<List<NModel>> listOfMessage;

    public DBUtil getDbutil() {
        if (dbutil == null) {
            dbutil = new DBUtil();
        }
        return dbutil;
    }

    public void setDbutil(DBUtil dbutil) {
        this.dbutil = dbutil;
    }

    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public NexmoReadClick(INotifier iNotifier, NexmoModel nexmoModel) {
        this.nexmoModel = nexmoModel;
        this.iNotifier = iNotifier;
    }

    public NexmoReadClick(NexmoModel inexmoModel) {
        this.nexmoModel = inexmoModel;
    }
    boolean firstClick = false;

    @Override
    public void run() {
        try {
            if (nexmoModel.isRepeated()) {
                while (true) {
                    if (nexmoModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("On work and waiting for " + nexmoModel.getWaitTime());
                    log.debug("On work and waiting for " + nexmoModel.getWaitTime());
                    Util.wait(nexmoModel.getWaitTime());
                    readMessage();
                    if (nexmoModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("On work and waiting for " + nexmoModel.getRepeatTime());
                    log.debug("On work and waiting for " + nexmoModel.getRepeatTime());
                    Util.wait(nexmoModel.getRepeatTime());
                }
            } else {
                readMessage();
            }
        } catch (Throwable ex) {
            iNotifier.notify("Nexmo:Run:: stopped for error: " + ExceptionUtils.getMessage(ex));
            log.error("Nexmo:Run:: stopped causes " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private void readMessage() {
        getDbutil();
        List<AuthInfo> keys = dbutil.getAuthorityInfoFromDb(nexmoModel.getUserid());
        if (keys == null || keys.isEmpty()) {
            iNotifier.notify("Auth table is empty.");
            log.debug("Auth table is empty.");
            return;
        }
        for (AuthInfo in : keys) {
            _readMessage(in);
        }
    }

    private void _readMessage(AuthInfo authInfo) {
        iNotifier.notify("Going to read message for : " + authInfo.getNumber());
        log.debug("Going to read message for : " + authInfo.getNumber());
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        String dateAsString = Urls.urlEncode(NexmoUtil.getCurrentTimeInUTCDate(date));
        dbutil = getDbutil();
        String uri = "https://rest.nexmo.com/search/messages/" + authInfo.getApiKey() + "/" + authInfo.getApiSecret() + "?date=" + dateAsString + "&to=" + authInfo.getNumber() + "";
        listOfMessage = readMessageFile(nexmoModel);
        if (listOfMessage == null || listOfMessage.isEmpty()) {
            iNotifier.notify("The message file is empty!");
            Util.wait(3);
            return;
        }
        try {
            String response = HttpHelper.get(new URL(uri));
            if (StringUtils.isBlank(response) || response.contains("Request failed.")) {
                iNotifier.notify("Nexmo::No message found");
                log.debug("Nexmo::No message found");
                Util.wait(1);
                return;
            }

            JSONObject responseAsJsonObject = new JSONObject(response);
            JSONArray messagesAsJson = responseAsJsonObject.getJSONArray("items");
            int messageCount = messagesAsJson.length() - 1;
            HashMap<String, JSONObject> messageMaps = new HashMap<String, JSONObject>();
            while (0 <= messageCount) {
                JSONObject messageJsonObject = messagesAsJson.getJSONObject(messageCount);
                messageCount = messageCount - 1;
                String fromNumber = messageJsonObject.getString("from");
                if (messageMaps.containsKey(fromNumber)) {
                    continue;
                }
                messageMaps.put(fromNumber, messageJsonObject);
            }

            for (String messageKey : messageMaps.keySet()) {
                JSONObject messageJsonObject = messageMaps.get(messageKey);
                int send_count = 1;
                long nextSendTime;
                long botSendingTime = 0;
                String from = messageJsonObject.getString("from");
                String to = messageJsonObject.getString("to");
                String message_id = messageJsonObject.getString("message-id");
                String account_id = messageJsonObject.getString("account-id");
                String network = messageJsonObject.getString("network");
                String body = messageJsonObject.getString("body");
                String date_recieved = messageJsonObject.getString("date-received");
                String type = messageJsonObject.getString("type");
                long recievedTime = getRecievedTime(date_recieved);
                getRandom();
                Inbound inb = dbutil.isExistingData(from, to, nexmoModel.getUserid());
                // if db is null its meant new record so make insert here.
                if (inb == null) {
                    iNotifier.notify("Nexmo::New message found from"+from );
                    log.debug("Nexmo::New message found from"+from );
                    List<NModel> list = listOfMessage.get(0);
                    nextSendTime = recievedTime + list.get(0).getWaitTimeInSec();
                    dbutil.insertData(0, send_count, message_id, type, from, to, account_id, body, date_recieved, network, nexmoModel.getUserid(), recievedTime, nextSendTime, authInfo.getApiKey(), authInfo.getApiSecret(), botSendingTime);
                    dbutil.UpdateAuthInfoGetMessage(authInfo);
                    continue;
                }
                // if here its meant update record   
                if (listOfMessage.size() < inb.getSend_count() + 1) {
                    iNotifier.notify("Message size is less than send count.");
                    continue;
                }
                List<NModel> list = listOfMessage.get((inb.getSend_count() + 1) - 1);
                nextSendTime = recievedTime + list.get(0).getWaitTimeInSec();
                // if received time less then continue from here
                if (inb.getRecieved_time() >= recievedTime) {
                    continue;
                }
                if (inb.getMessage_status() == 0) {
                    continue;
                }
                // If previoud and current message same then get back from here.
//                if (inb.getBody().equalsIgnoreCase(body)) {
//                    continue;
//                }

                if (inb.getLast_send_time() > recievedTime) {
                    continue;
                }
                inb.setBody(body);
                inb.setRecieved_time(recievedTime);
                inb.setDate_recieved(date_recieved);
                inb.setSend_count(inb.getSend_count() + 1);
                inb.setMessage_status(0);
                inb.setNext_send_time(nextSendTime);
                iNotifier.notify("Updating data---");
                log.debug("Updating data---");
                dbutil.updateDataForMsgStatusOne(inb);
                dbutil.UpdateAuthInfoGetMessage(authInfo);
            }
            iNotifier.notify("Nexmo::Message store Done!!!");
            log.debug("Nexmo::Message store Done!!!");
        } catch (Exception ex) {
            iNotifier.notify("Nexmo::Error on Reading Message" + ExceptionUtils.getMessage(ex));
            log.debug("Nexmo::Error on Reading Message" + ExceptionUtils.getStackTrace(ex));
        }
    }

    public List<List<NModel>> readMessageFile(NexmoModel m) {
        try {
            iNotifier.notify("Nexmo: Reading Message file");
            log.debug("Nexmo:: Reading Message file.....");
            List<List<NModel>> nModels = new ArrayList<List<NModel>>();
            FileInputStream file;
            if (StringUtils.isNotBlank(m.getMessageFile())) {
                file = new FileInputStream(m.getMessageFile());
            } else {
                file = new FileInputStream("C:\\nexmo\\message.xls");
            }
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isHeader = true;
            while (rowIterator.hasNext()) {
                List<NModel> model = new ArrayList<NModel>();
                if (isHeader) {
                    isHeader = false;
                    rowIterator.next();
                    continue;
                }
                Row row = rowIterator.next();
                if (row.getCell(0) == null) {
                    return nModels;
                }
                BigDecimal d = new BigDecimal(0.0);
                if (row.getCell(2) != null) {
                    d = new BigDecimal(row.getCell(2).toString().trim());
                }
                if (row.getCell(0) != null) {
                    String msgs = row.getCell(0).toString().trim();
                    String[] msg = msgs.split("@@@");
                    for (String s : msg) {
                        NModel nModel = new NModel();
                        nModel.setMessage(s);
                        nModel.setWaitTimeInSec(d.longValue());
                        model.add(nModel);
                    }
                }
                nModels.add(model);
            }
            iNotifier.notify("Nexmo: Reading Message file. Done");
            log.debug("Nexmo:: Reading Message file. Done");
            return nModels;
        } catch (Throwable ex) {
            iNotifier.notify("Error on Reading xls File" + ExceptionUtils.getMessage(ex));
            log.debug("Error on Reading xls File" + ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }

    public long getRecievedTime(String date_recieved) {
        long receivedTimeInSec = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date _date = formatter.parse(date_recieved);
            long inGmt = 0; //6 * 60 * 60 * 1000;
            receivedTimeInSec = (_date.getTime() + inGmt) / 1000;
        } catch (ParseException ex) {
            iNotifier.notify("Error on Nexmo processing" + ExceptionUtils.getMessage(ex));
            log.debug("Error on Nexmo processing" + ExceptionUtils.getStackTrace(ex));
        }
        return receivedTimeInSec;
    }

    public static void main(String s[]) {

    }
}
