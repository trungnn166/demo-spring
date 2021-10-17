package com.nnt.demo.services.impl;

import com.nnt.demo.model.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Value("${config.mail.host}")
    private String host;
    @Value("${config.mail.port}")
    private String port;
    @Value("${config.mail.username}")
    private String email;
    @Value("${config.mail.password}")
    private String password;

    @Autowired
    EmailTemplateService emailTemplateService;

    public void sendMail(EmailModel emailModel) {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        Message message = new MimeMessage(session);
        try {
            message.setRecipients(Message.RecipientType.TO,
                    new InternetAddress[] { new InternetAddress(emailModel.getEmailTo()) });

            message.setFrom(new InternetAddress(email));
            message.setSubject(emailModel.getSubject());
            message.setContent(emailTemplateService.getContent(emailModel), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(String email, String subject, String template) {
        sendMail(new EmailModel(email, subject, template, new Context()));
    }
}
