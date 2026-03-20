package com.knock.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static FrameWorkPackage.com.rp.automation.framework.util.CommonUtils.generateRandomString;
import static FrameWorkPackage.com.rp.automation.framework.util.CommonUtils.generateRandomString;

public class EmailUtils {

    public static void sendEmail(String fromAddressName, String username, String password, Map<String, String> data) {
        try {
            String[] mailTo = data.get("EmailTo").split(";");
            String[] mailCc;

            try {
                mailCc = data.get("EmailCc").split(";");
            } catch (NullPointerException emailCcNull) {
                mailCc = null;
            }

            String subject = data.get("EmailSubject");
            Properties properties = new Properties();

            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String gmailAddress = username + "+" + fromAddressName + "@gmail.com";


            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(username, fromAddressName));
            msg.setReplyTo(new Address[]{new InternetAddress(gmailAddress, fromAddressName)});

            for (String string : mailTo) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(string));
            }

            if (mailCc != null) {
                for (String s : mailCc) {
                    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(s));
                }
            }

            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(data.get("EmailBody"), "text/html");
            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateFromName() {
        return "Test_" + generateRandomString(7);
    }
}