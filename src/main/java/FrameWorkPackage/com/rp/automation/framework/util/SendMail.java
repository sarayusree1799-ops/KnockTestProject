package FrameWorkPackage.com.rp.automation.framework.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;

import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

public class SendMail {
    public static <mailFlag> void sendHtmlEmail(String host, String port, String userName, String[] toAddress, String[] ccAddress, String subject, String message, boolean mailFlag) throws AddressException, MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "false");
        properties.put("mail.smtp.starttls.enable", "false");
        Session session = Session.getInstance(properties);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));

        for(int i = 0; i < toAddress.length; ++i) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress[i]));
        }

        for(int i = 0; i < ccAddress.length; ++i) {
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress[i]));
        }

        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/html");
        if (!mailFlag) {
            msg.addHeader("X-Priority", "1");
            msg.setFlag(Flags.Flag.FLAGGED, true);
        }
        Transport.send(msg);
    }

    public static boolean sendMail(String userName, String passWord, String host, String port, String starttls, String auth, boolean debug, String socketFactoryClass, String fallback, String[] to, String[] cc, String[] bcc, String subject, String text, String attachmentPath, String attachmentName) {
        Properties props = new Properties();
        props.put("mail.smtp.user", userName);
        props.put("mail.smtp.host", host);
        if (!"".equals(port)) {
            props.put("mail.smtp.port", port);
        }
        if (!"".equals(starttls)) {
            props.put("mail.smtp.starttls.enable", starttls);
            props.put("mail.smtp.auth", auth);
        }
        if (debug) {
            props.put("mail.smtp.debug", "true");
        } else {
            props.put("mail.smtp.debug", "false");
        }
        if (!"".equals(port)) {
            props.put("mail.smtp.socketFactory.port", port);
        }
        if (!"".equals(socketFactoryClass)) {
            props.put("mail.smtp.socketFactory.class", socketFactoryClass);
        }
        if (!"".equals(fallback)) {
            props.put("mail.smtp.socketFactory.fallback", fallback);
        }
        try {
            Session session = Session.getDefaultInstance(props, (Authenticator) null);
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            File att = new File(new File(attachmentPath), attachmentName);
            DataSource source = new FileDataSource(att.getPath());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(att.getName());
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress(userName));
            for (int i = 0; i < to.length; ++i) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
            }
            for (int i = 0; i < cc.length; ++i) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
            }
            for (int i = 0; i < bcc.length; ++i) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
            }
            MimeBodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(text);
            multipart.addBodyPart(messageBodyPart1, 0);
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(host, userName, passWord);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception mex) {
            mex.printStackTrace();
            return false;
        }
    }
}
