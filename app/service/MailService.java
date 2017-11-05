package service;


import models.GroupOrder;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

public class MailService {
    public void sendGroupDemandMail(String title, String htmlBody) {
       /* Example
       Email email = new Email();
        email.setSubject("Simple email");
        email.setFrom("Mister FROM <from@email.com>");
        email.addTo("Miss TO <to@email.com>");
        // adds attachment
        email.addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"));
        // adds inline attachment from byte array
        email.addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE);
        // sends text, HTML or both...
        email.setBodyText("A text message");
        email.setBodyHtml("<html><body><p>An <b>html</b> message</p></body></html>");*/
        Email email = new Email();
        email.setSubject(title);
        email.setFrom("dannyzjwz@163.com");
        email.addTo("dannyzjwz@163.com");

        email.setBodyHtml(htmlBody);
        MailerPlugin.send(email);
    }
}
