package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Folder;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Flags.Flag;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class GMailUtils {
    public GMailUtils() {
    }

    public String getemail(String subject, final String mailid, final String pwd) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mail.realpage.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.host", "mail.realpage.com");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.auth.login.disable", "false");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailid, pwd);
            }
        });
        session.setDebug(true);
        Store store = session.getStore("imap");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        store.connect("outlook.office365.com", 993, mailid, pwd);
        String bf = "";
        Folder folder = store.getFolder("INBOX");
        folder.open(2);
        System.out.println("Total Message: " + folder.getMessageCount());
        System.out.println("Unread Message: " + folder.getUnreadMessageCount());
        Message[] messages = null;
        boolean isMailFound = false;
        Message mailFromGod = null;
        SearchTerm searchTerm = new SubjectTerm(subject);
        messages = folder.search(searchTerm);
        Message[] var13 = messages;
        int var14 = messages.length;

        for (int var15 = 0; var15 < var14; ++var15) {
            Message mail = var13[var15];
            if (!mail.isSet(Flag.SEEN)) {
                mailFromGod = mail;
                System.out.println("Message Count is: " + mail.getMessageNumber());
                isMailFound = true;
            }
        }

        if (!isMailFound) {
            throw new Exception("Could not find new mail from God :-(");
        } else {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(mailFromGod.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            bf = buffer.toString();
            System.out.println(bf);
            return bf;
        }
    }
}