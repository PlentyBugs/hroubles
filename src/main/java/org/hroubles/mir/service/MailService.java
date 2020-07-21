package org.hroubles.mir.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage smm = new SimpleMailMessage();

        smm.setFrom(username);
        smm.setTo(emailTo);
        smm.setSubject(subject);
        smm.setText(message);

        mailSender.send(smm);
    }
}
