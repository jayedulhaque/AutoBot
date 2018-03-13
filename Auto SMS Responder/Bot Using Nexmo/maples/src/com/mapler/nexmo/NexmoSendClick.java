package com.mapler.nexmo;

import com.mapler.service.INotifier;
import com.mapler.utility.HttpHelper;
import com.mapler.utility.Util;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.net.Urls;

/**
 *
 * @author JAYED
 */
public class NexmoSendClick implements Runnable {

    private NexmoModel nexmoModel;
    private static Logger log = Logger.getLogger(NexmoReadClick.class);
    private INotifier iNotifier;
    private Random random;
    public DBUtil db;
    List<List<NModel>> listOfMessage;

    @Override
    public void run() {
        try {
            if (nexmoModel.isRepeated()) {
                while (true) {
                    if (nexmoModel.isStopEngine()) {
                        break;
                    }
                    doOnNexmo();
                    if (nexmoModel.isStopEngine()) {
                        break;
                    }
                    iNotifier.notify("On work and waiting for " + nexmoModel.getRepeatTime());
                    log.error("On work and waiting for " + nexmoModel.getRepeatTime());
                    Util.wait(nexmoModel.getRepeatTime());
                }
            } else {
                doOnNexmo();
            }
        } catch (Throwable ex) {
            iNotifier.notify("Nexmo:Run:: stopped for error: " + ExceptionUtils.getMessage(ex));
            log.error("Nexmo:Run:: stopped causes " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private void doOnNexmo() {
        try {
            iNotifier.notify("Nexmo: Praparing to send message via Nexmo");
            log.debug("Nexmo: Going to send message...");
            iNotifier.notify("Nexmo: Reading data from server to send message");
            db = getDb();
            List<Inbound> inboundMessages = db.getDataByNumberUsingStatus(nexmoModel.getUserid());
            iNotifier.notify("Nexmo: Reading data from server has been done");
            if (inboundMessages == null) {
                log.error("No data found in server to send message");
                iNotifier.notify("No data found in server to send message");
                Util.wait(3);
                return;
            }

            iNotifier.notify("Nexmo: Reading message text from file.");
            listOfMessage = readMessageFile(nexmoModel);
            iNotifier.notify("Nexmo: Reading message text from file has been done");
            if (listOfMessage == null || listOfMessage.isEmpty()) {
                iNotifier.notify("Message file is empty. Getting back");
                log.debug("Message file is empty. Getting back");
                Util.wait(3);
                return;
            }
            getRandom();
            sendMessage(db, inboundMessages);
        } catch (Throwable ex) {
            iNotifier.notify("Nexmo:Run:: stopped for error: " + ExceptionUtils.getMessage(ex));
            log.debug("Nexmo:Run:: stopped for error: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private void sendMessage(DBUtil db, List<Inbound> inboundMessages) {
        try {
            iNotifier.notify("Preparing connection to server");
            for (Inbound inboundMessage : inboundMessages) {
                int size = listOfMessage.size();
                int sendCount = inboundMessage.getSend_count();
                long nextSendTime = inboundMessage.getNext_send_time();
                long nowTime = NexmoUtil.getCurrentTimeInUTCSecond(System.currentTimeMillis());
                if (size < sendCount) {
                    continue;
                }
                if (nowTime < nextSendTime) {
                    continue;
                }
                String status = null;
                String fNumber = inboundMessage.getMessage_from();
                iNotifier.notify("Sending message for " + fNumber);
                String tNumber = inboundMessage.getMessage_to();
                String apiKey = inboundMessage.getApi_key();
                String apiSecret = inboundMessage.getApi_secret();
                List<NModel> list = listOfMessage.get(sendCount - 1);
                String body = list.get(random.nextInt(list.size())).getMessage();
                String param = "api_key=" + apiKey + "&api_secret=" + apiSecret + "&from=" + tNumber + "&to=" + fNumber + "&text=" + Urls.urlEncode(body);
                String uri = "https://rest.nexmo.com/sms/json?";
                String response = HttpHelper.sendGet(uri + param, "");
                JSONObject responseAsJsonObject = new JSONObject(response);
                JSONArray messagesAsJson = responseAsJsonObject.getJSONArray("messages");
                int messageCount = messagesAsJson.length() - 1;
                while (0 <= messageCount) {
                    JSONObject messageJsonObject = messagesAsJson.getJSONObject(messageCount);
                    messageCount = messageCount - 1;
                    status = messageJsonObject.getString("status");
                }
                if (StringUtils.isBlank(response)) {
                    iNotifier.notify("Unable to send message for " + fNumber);
                    log.error("Unable to send message for " + response);
                    Util.wait(2);
                    continue;
                } else if (!response.contains("message-price")) {
                    iNotifier.notify("Unable to send message for " + fNumber);
                    log.error("Unable to send message for " + response);
                    db.updateNotSentMessageStatus(fNumber, tNumber, status);
                    Util.wait(2);
                    continue;
                }
                Util.wait(1);
                long lastSendTimeInSec = NexmoUtil.getCurrentTimeInUTCSecond(System.currentTimeMillis());
                db.updateMessageStatus(fNumber, lastSendTimeInSec, tNumber, status);
                Util.waitInMiliSecond(100);
                iNotifier.notify("Sending message has been done for " + fNumber);
            }
        } catch (SQLException ex) {
            iNotifier.notify("Nexmo:Run:: stopped for sending error: " + ExceptionUtils.getMessage(ex));
            log.debug("Nexmo:Run:: stopped sending for error: " + ExceptionUtils.getStackTrace(ex));
        } catch (Exception ex) {
            iNotifier.notify("Nexmo:Run:: stopped for sending error: " + ExceptionUtils.getMessage(ex));
            log.debug("Nexmo:Run:: stopped for sending error: " + ExceptionUtils.getStackTrace(ex));
        }
        iNotifier.notify("Sending done.");
        log.debug("Sending done.");
    }

    public List<List<NModel>> readMessageFile(NexmoModel m) {
        try {
            iNotifier.notify("Nexmo: Reading Message file");
            log.debug("Nexmo: Reading Message file");
            List<List<NModel>> nModels = new ArrayList<List<NModel>>();
            FileInputStream file = new FileInputStream("C:\\nexmo\\message.xls");
            if (StringUtils.isNotBlank(m.getMessageFile())) {
                file = new FileInputStream(m.getMessageFile());
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
            log.debug("Nexmo: Reading Message file. Done");
            return nModels;
        } catch (Throwable ex) {
            iNotifier.notify("" + ExceptionUtils.getMessage(ex));
            log.debug("#############################" + ExceptionUtils.getStackTrace(ex));
        }
        return null;
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

    public NexmoSendClick(INotifier iNotifier, NexmoModel nexmoModel) {
        this.nexmoModel = nexmoModel;
        this.iNotifier = iNotifier;
    }

    public NexmoSendClick(NexmoModel inexmoModel) {
        this.nexmoModel = inexmoModel;
    }

    public DBUtil getDb() {
        if (db == null) {
            db = new DBUtil();
        }
        return db;
    }

    public void setDb(DBUtil db) {
        this.db = db;
    }
}
