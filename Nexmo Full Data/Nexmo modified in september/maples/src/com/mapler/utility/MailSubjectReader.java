package com.mapler.utility;

import com.mapler.service.INotifier;
import java.util.*;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class MailSubjectReader {

    private static Logger log = Logger.getLogger(MailSubjectReader.class);
    Folder inbox;
    Store store;
    private INotifier iNotifier;
    EMailModel eMailModel;

    public MailSubjectReader(EMailModel eMailModel, INotifier iNotifier) {
        this.iNotifier = iNotifier;
        this.eMailModel = eMailModel;
    }

    public ArrayList<String> checkMail() {
        ArrayList<String> rLinks = new ArrayList<String>();
        try {
            /* Set the mail properties */
            iNotifier.notify("Connecting to email server...");

            String email = eMailModel.getEmail();
            String password = eMailModel.getEmailPassword();

            if (email.contains("+")) {
                String[] ee = email.split("\\+");
                if (email.contains("@gmail.com")) {
                    email = ee[0] + email.split("@")[1];
                }
            }

            if (email == null || email.isEmpty()) {
                iNotifier.notify("Email address is empty.");
                log.debug("run:: Email address is empty. ");
                return rLinks;
            }

            if (password == null || password.isEmpty()) {
                iNotifier.notify("Email password is empty.");
                log.debug("run:: Email password is empty. ");
                return rLinks;
            }

            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            /* Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(props, null);

            store = session.getStore("imaps");
            if (StringUtils.isNotBlank(eMailModel.getProtocal())) {
                store = session.getStore(eMailModel.getProtocal().trim());
            }

            if (email.contains("@gmail")) {
                store.connect("imap.gmail.com", email, password);
            } else if (email.contains("@yahoo")) {
                store.connect("imap.mail.yahoo.com", email, password);
            } else {
                store.connect(eMailModel.getMailServer(), email, password);
            }

            iNotifier.notify("Reading email from server...");
            /* Mention the folder name which you want to read. */
            inbox = store.getFolder("Inbox");
            int unReadMCount = inbox.getUnreadMessageCount();
            iNotifier.notify("No of Unread Messages : " + unReadMCount);
            if (unReadMCount == 0) {
                return rLinks;
            }
            /* Open the inbox using store. */
            if (eMailModel.isMailReadAndUpdateOnly()) {
                inbox.open(Folder.READ_WRITE);
            } else {
                inbox.open(Folder.READ_ONLY);
            }
            
            /* Get the messages which is unread in the Inbox */
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);
            try {
                for (int i = 0; i < messages.length; i++) {
                    Message message = messages[i];
                    String sub = message.getSubject();
                    rLinks.add(sub);
                }
                if (eMailModel.isMailReadAndUpdateOnly()) {
                    inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
                }
                inbox.close(true);
                store.close();
                return rLinks;
            } catch (Throwable ex) {
                log.debug("readMail :: Exception arise at the time of read mail " + ex);
                iNotifier.notify("Exception arise at the time of read mail");
                ex.printStackTrace();
            }
        } catch (NoSuchProviderException ex) {
            log.debug("readMail :: Error on NoSuchProviderException " + ex);
            iNotifier.notify("Exception arise at the time of read mail");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            log.debug("readMail :: Error on MessagingException " + ex);
            iNotifier.notify("Exception arise at the time of read mail");
            ex.printStackTrace();
        } catch (Throwable ex) {
            log.debug("readMail :: Error on MessagingException " + ex);
            iNotifier.notify("Exception arise at the time of read mail");
            ex.printStackTrace();
        }

        return rLinks;
    }
}
