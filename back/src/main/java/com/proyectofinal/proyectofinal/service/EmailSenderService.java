package com.proyectofinal.proyectofinal.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.proyectofinal.proyectofinal.utils.EmailMessageBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class EmailSenderService {
        @Autowired
        private Gmail gmailService;

    
        public void sendEmail(String to, String subject, String bodyText) {
                try {
                        log.info(to, " ", subject, " ", bodyText);
                        sendEmail("me", to, "proyectofinalgp1@gmail.com", subject, bodyText);
                } catch (Exception e) {
                        log.error("Error al tratar de enviar el mail", e);
                }
        }

         private Message sendEmail(String userId, String to, String from, String subject, String bodyText)
                        throws IOException {
                Message message = EmailMessageBuilder.Build(to, from, subject, bodyText);
                message = gmailService.users().messages().send(userId, message).execute();
                log.info("Sent email with ID: " + message.getId());
                return message;
        }
}
