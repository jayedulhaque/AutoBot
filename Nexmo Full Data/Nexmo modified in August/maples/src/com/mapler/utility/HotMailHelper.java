package com.mapler.utility;

import com.mapler.model.SModel;
import com.mapler.model.UAModel;
import com.mapler.service.INotifier;
import java.util.Properties;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Mithun
 */
public class HotMailHelper {

    private static Logger log = Logger.getLogger(HotMailHelper.class);

    public synchronized void readNGFXHotmail(WebDriver driver, SModel sModel, UAModel uam, INotifier iNotifier) {
        try {
            /* Set the mail properties */
            iNotifier.notify("Going to read email inbox");

            String email = uam.getEmail();
            String password = uam.getEmailPassword();
            if (sModel.getUseFWlink().equals("1")) {
                email = uam.getForwardEmail();
                password = uam.getForwardEmailPass();
            }

            if (email.contains("+")) {
                String[] ee = email.split("\\+");
                email = ee[0] + "@hotmail.com";
            }

            if (email == null || email.isEmpty()) {
                iNotifier.notify("Email address is empty.");
                log.debug("run:: Email address is empty. ");
                return;
            }

            if (password == null || password.isEmpty()) {
                iNotifier.notify("Email password is empty.");
                log.debug("run:: Email password is empty. ");
                return;
            }

            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "pop3s");

            /* Create the session and get the store for read the mail. */
            iNotifier.notify("Going to connect mail server");
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("pop3s");
            store.connect("pop3.live.com", email, password);

            /* Mention the folder name which you want to read. */
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
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
                    parseNGFXMessage(driver, message,uam,iNotifier);
                }
                inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
                inbox.close(true);
                store.close();
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
    }

    private void parseNGFXMessage(WebDriver driver, Message message,UAModel uam, INotifier iNotifier) throws Exception {
        try {
            String sub = message.getSubject();
            if (!sub.contains("Action Required to Activate Membership for Forex")) {
                return;
            }
            String contentType = message.getContentType();
            System.out.println("Content Type : " + contentType);
            if (message.getContent() instanceof Multipart) {
                Multipart mp = (Multipart) message.getContent();
                int count = mp.getCount();
                for (int j = 0; j < count; j++) {
                    String s = mp.getBodyPart(j).getContent().toString();
                    String lines[] = s.split("\\r?\\n");
                    for (int k = 0; k < lines.length; k++) {
                        if (lines[k].contains("http://nigeria-forex.com/register.php") && lines[k].startsWith("http")) {
                            log.debug("readWebGmail :: Posting link found " + lines[k]);
                            iNotifier.notify("Posting link found now going to click on this link [" + count + "]");
                            driver.get(lines[k]);
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
                    if (link.text().contains("http://nigeria-forex.com/register.php")) {
                        log.debug("readWebGmail :: Posting link found " + link.text());
                        iNotifier.notify("Posting link found now going to click on this link ");
                        driver.get(link.text());
                        uam.setMailFound(true);
                        break;
                    }

                }
            }

        } catch (Throwable ex) {
            log.debug("readAllMessages :: Error " + ex);
            iNotifier.notify("Exception arise at the time of parsing email.");
            ex.printStackTrace();
        }
    }
}
