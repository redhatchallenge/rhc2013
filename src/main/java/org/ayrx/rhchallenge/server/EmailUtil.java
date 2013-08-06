package org.ayrx.rhchallenge.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author: Terry Chia (Ayrx)
 */
public class EmailUtil {

    private static HtmlEmail mail;

    public static void sendEmail(String subject, String htmlMessage, String textMessage, String recipient) {

        mail = new HtmlEmail();

        try {
            mail.setHostName("smtp.gmail.com");
            mail.setSmtpPort(465);
            mail.setSSLOnConnect(true);
            mail.setAuthentication("test.rhc2013@gmail.com", "redhatchallenge2013");
            mail.setFrom("rh@rh.com");
            mail.setSubject(subject);
            mail.setTextMsg(textMessage);
            mail.setHtmlMsg(htmlMessage);
            mail.addTo(recipient);

            mail.send();

        } catch (EmailException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public static String generateToken(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}