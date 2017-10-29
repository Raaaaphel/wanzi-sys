package service;


import models.GroupOrder;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

public class MailService {
    public void sendGroupDemandMail(GroupOrder order) {
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
        email.setSubject("团游定制_" + order.getFirmName() + "_" + order.getUserName());
        email.setFrom("dannyzjwz@163.com");
        email.addTo("dannyzjwz@163.com");
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<html><body><p>企业名称：");
        bodyBuilder.append(order.getFirmName());
        bodyBuilder.append("</p><p>联系人：");
        bodyBuilder.append(order.getUserName());
        bodyBuilder.append("</p><p>联系电话：");
        bodyBuilder.append(order.getPhone());
        bodyBuilder.append("</p><p>邮箱：");
        bodyBuilder.append(order.getUserEmail());
        bodyBuilder.append("</p><p>需求内容：");
        bodyBuilder.append(order.getContent());
        bodyBuilder.append("</p></body><html>");
        email.setBodyHtml(bodyBuilder.toString());
        MailerPlugin.send(email);
    }
}
