package com.mapler.utility;

import com.mapler.model.SModel;
import com.mapler.model.UAModel;
import com.mapler.service.INotifier;
import java.util.*;
import javax.mail.*;
import org.apache.log4j.Logger;

public class MailReader {

    Folder inbox;
    private SModel sModel;
    private static Logger log = Logger.getLogger(MailReader.class);
    private INotifier iNotifier;
    UAModel uam;
    private HotMailHelper hotMailHelper;
    private GMaiHelper gmailHelper;
    private HashMap<String,String> mailMap = new HashMap<String, String>();
    
    public MailReader(INotifier iNotifier, SModel sModel, UAModel uam,HashMap<String,String> mailMap) {
        this.sModel = sModel;
        this.iNotifier = iNotifier;
        this.uam = uam;
        this.mailMap = mailMap;
    }

    public ArrayList<String> checkMail() {
        try {
            String email = uam.getEmail();
            String password = uam.getEmailPassword();
            if (sModel.getUseFWlink().equals("1")) {
                email = uam.getForwardEmail();
                password = uam.getForwardEmailPass();
            }
            if (!email.contains("@")) {
                iNotifier.notify("Email address is not valid.");
                log.debug("Emal address is not valid.");
                return null; 
            }

            if (email.contains("+")) {
                String[] ee = email.split("\\+");
                email = ee[0] + "@gmail.com";
            }

            if (email == null || email.isEmpty()) {
                iNotifier.notify("Email address is empty.");
                log.debug("run:: Email address is empty. ");
                return null;
            }

            if (password == null || password.isEmpty()) {
                iNotifier.notify("Email password is empty.");
                log.debug("run:: Email password is empty. ");
                return null;
            }

            String[] ee = email.split("@");
            if (ee[1].equals("gmail.com")) {
                iNotifier.notify("Gmail found going to read gmail.");
                return this.getGmailHelper().readGmail(email, password);
            } else if (ee[1].equals("hotmail.com")) {
            }
            
        } catch (Throwable ex) {
        }
        return null;
    }

    private HotMailHelper getHotMailHelper() {
        if (hotMailHelper == null) {
            hotMailHelper = new HotMailHelper();
        }
        return hotMailHelper;
    }
    
    private GMaiHelper getGmailHelper() {
        if (gmailHelper == null) {
            gmailHelper = new GMaiHelper(iNotifier, sModel, uam,mailMap);
        }
        return gmailHelper;
    }
}
