package com.mapler.service;

import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import org.apache.log4j.Logger;

public class MailReader {

    Folder inbox;
    private static Logger log = Logger.getLogger(MailReader.class);
    
    // Constructor of the calss.
    public void readMail() {
        /* Set the mail properties */
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            ArrayList<String> mail = new ArrayList<String>();
            /* Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            //store.connect("imap.gmail.com", "skyroproject8@gmail.com", "mn142536");
            store.connect("mail.clirobot.com", "robot@clirobot.com", "r@b@t");

            /* Mention the folder name which you want to read. */
            inbox = store.getFolder("Inbox");
            System.out.println("No of Unread Messages : " + inbox.getUnreadMessageCount());

            /* Open the inbox using store. */
            inbox.open(Folder.READ_ONLY);
            ////inbox.open(Folder.READ_WRITE);
            /* Get the messages which is unread in the Inbox */
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);
            try {
                readAllMessages(messages,mail);

                inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
                inbox.close(true);
                store.close();
            } catch (Exception ex) {
                System.out.println("Exception arise at the time of read mail");
                ex.printStackTrace();
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public void readAllMessages(Message[] msgs, ArrayList mail) throws Exception {
        try {
            for (int i = 0; i < msgs.length; i++) {
                System.out.println("MESSAGE #" + (i + 1) + ":");
                Message message = msgs[i];
                String contentType = message.getContentType();
                System.out.println("Content Type : " + contentType);
                String sub = message.getSubject();
                System.out.println("subject ::::::::::::"+sub);
                Multipart mp = (Multipart) message.getContent();
                int count = mp.getCount();
                for (int j = 0; j < count; j++) {
                    String s = mp.getBodyPart(j).getContent().toString();
                    String lines[] = s.split("\\r?\\n");
                    for (int k = 0; k <= lines.length; k++) {
                        if (lines[k].contains("https://post.craigslist.org") && lines[k].startsWith("https")) {
                            mail.add(lines[k]);
                            System.out.println(":::::::::::" + lines[k]);
                            break;
                        }
                    }
                }
            }
        } catch (Throwable ex) {
        }

    }

    public static void main(String args[]) {
        new MailReader().readMail();
    }
}
