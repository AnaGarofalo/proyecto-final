package com.proyectofinal.proyectofinal.service;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.proyectofinal.proyectofinal.utils.EmailMessageBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String EMAIL;

    public void sendEmail(String to, String subject, String bodyText) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(EMAIL); 
            message.setTo(to);
            message.setSubject(subject);
            message.setText(bodyText);

            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending email to: {}", e.getMessage(), e);
        }
    }
}
