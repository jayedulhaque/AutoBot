package com.mapler.utility;

import com.mapler.model.SModel;
import com.mapler.model.UAModel;
import com.mapler.service.INotifier;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GMaiHelper {

    Folder inbox;
    private SModel sModel;
    private static Logger log = Logger.getLogger(GMaiHelper.class);
    private INotifier iNotifier;
    UAModel uam;
    private HashMap<String, String> mailMap = new HashMap<String, String>();

    public GMaiHelper(INotifier iNotifier, SModel sModel, UAModel uam, HashMap<String, String> mailMap) {
        this.sModel = sModel;
        this.iNotifier = iNotifier;
        this.uam = uam;
        this.mailMap = mailMap;
    }

    public ArrayList<String> readGmail(String email, String password) {
        try {
            if (!sModel.isWebMail()) {
                return this.readInGmail(email, password);
            } else {
                return this.readWebGmail(email, password);
            }
        } catch (Throwable ex) {
        }
        return null;
    }

    private ArrayList<String> readInGmail(String email, String password) {
        ArrayList<String> rLinks = new ArrayList<String>();
        try {
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            /* Create the session and get the store for read the mail. */
            //iNotifier.notify("Going to connect mail server");
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", email, password);

            /* Mention the folder name which you want to read. */
            inbox = store.getFolder("Inbox");
            int unReadMCount = inbox.getUnreadMessageCount();
            //iNotifier.notify("No of Unread Messages : " + unReadMCount);
            if (unReadMCount == 0) {
                return null;
            }
            /* Open the inbox using store. */
            //inbox.open(Folder.READ_ONLY);
            inbox.open(Folder.READ_WRITE);
            /* Get the messages which is unread in the Inbox */
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);
            try {
                for (int i = 0; i < messages.length; i++) {
                    Message message = messages[i];
                    this.parseMessage(message, rLinks);
                }
                inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
                inbox.close(true);
                store.close();
            } catch (Throwable ex) {
                log.debug("readMail :: Exception arise at the time of read mail " + ex);
                //iNotifier.notify("Exception arise at the time of read mail");
                ex.printStackTrace();
            }
        } catch (NoSuchProviderException ex) {
            log.debug("readMail :: Error on NoSuchProviderException " + ex);
            //iNotifier.notify("Exception arise at the time of read mail");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            log.debug("readMail :: Error on MessagingException " + ex);
            //iNotifier.notify("Exception arise at the time of read mail");
            ex.printStackTrace();
        } catch (Throwable ex) {
            log.debug("readMail :: Error on MessagingException " + ex);
            //iNotifier.notify("Exception arise at the time of read mail");
            ex.printStackTrace();
        }
        return rLinks;
    }

    public void parseMessage(Message message, ArrayList<String> rLinks) throws Exception {
        try {
            String sub = message.getSubject();
            if (!sub.contains(mailMap.get("subject"))) {
                return;
            }
            String contentType = message.getContentType();
            System.out.println("Content Type : " + contentType);
            if (contentType.contains("TEXT/PLAIN")) {
                String s = message.getContent().toString();
                String lines[] = s.split("\\r?\\n");
                for (int k = 0; k < lines.length; k++) {
                    if (lines[k].contains(mailMap.get("link")) && (lines[k].startsWith("https") || lines[k].startsWith("http"))) {
                        log.debug("readMail :: Posting link found " + lines[k]);
                        //iNotifier.notify("Posting link found");
                        rLinks.add(lines[k]);
                        uam.setMailFound(true);
                        break;
                    }
                }
            } else if (message.getContent() instanceof Multipart) {
                Multipart mp = (Multipart) message.getContent();
                int count = mp.getCount();
                for (int j = 0; j < count; j++) {
                    String s = mp.getBodyPart(j).getContent().toString();
                    String lines[] = s.split("\\r?\\n");
                    for (int k = 0; k < lines.length; k++) {
                        if (lines[k].contains(mailMap.get("link")) && (lines[k].startsWith("https") || lines[k].startsWith("http"))) {
                            log.debug("readMail :: Posting link found " + lines[k]);
                            //iNotifier.notify("Posting link found");
                            rLinks.add(lines[k]);
                            uam.setMailFound(true);
                            break;
                        }
                    }
                }
            } else {
                String html = message.getContent().toString();
                org.jsoup.nodes.Document doc = Jsoup.parse(html);
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    if (link.text().contains(mailMap.get("link"))) {
                        log.debug("readWebGmail :: Posting link found " + link.text());
                        //iNotifier.notify("Posting link found ");

                        rLinks.add(link.text());
                        uam.setMailFound(true);
                        break;
                    }
                }
            }

        } catch (Throwable ex) {
            log.debug("readAllMessages :: Error " + ex);
            //iNotifier.notify("Exception arise at the time of parsing email.");
            ex.printStackTrace();
        }
    }

    private ArrayList<String> readWebGmail(String email, String password) {
        ArrayList<String> rLinks = new ArrayList<String>();
        return rLinks;
    }
}
