package com.venduit.assetmanagement.user_service.service;


import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;


/**
 * This class is used to send email with and without attachment.
 * @author w3spoint
 */

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String EMAIL_SENDER;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine thymeleafTemplateEngine;

    public void sendSimpleMessage(String to, String subject, String body){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(EMAIL_SENDER);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
    @Job
    public void sendMessageUsingThymeleafTemplate(
            String to, String subject, Map<String, Object> templateModel, String emailType)
            throws MessagingException, MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(emailType, thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException, MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(EMAIL_SENDER);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
//        helper.addInline("attachment.png", resourceFile);
        javaMailSender.send(message);
    }

    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          ByteArrayInputStream inputStream,
                                          String attachmentFilename) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(EMAIL_SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            InputStreamSource attachmentSource = new ByteArrayResource(inputStream.readAllBytes());
            helper.addAttachment(attachmentFilename, attachmentSource);

            javaMailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}