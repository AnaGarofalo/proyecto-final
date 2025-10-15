package com.proyectofinal.proyectofinal.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.proyectofinal.proyectofinal.utils.EmailMessageBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class EmailSenderService {
        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        
        @Autowired
        private EmailCredentialBuilderService emailCredentialBuilderService;

        public void sendEmail(String to, String subject, String bodyText) {
                try {
                        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, emailCredentialBuilderService.getCredentials(HTTP_TRANSPORT))
                                        .setApplicationName("Proyecto Final - Grupo 1")
                                        .build();
                        log.info(to, " ", subject, " ", bodyText);
                        sendEmail(service, "me", to, "proyectofinalgp1@gmail.com", subject, bodyText);
                } catch (Exception e) {
                        log.error("Error al tratar de enviar el mail", e);
                }
        }

        public Message sendEmail(Gmail service, String userId, String to, String from, String subject, String bodyText)
                        throws IOException {
                Message message = EmailMessageBuilder.Build(to, from, subject, bodyText);
                message = service.users().messages().send(userId, message).execute();
                log.info("Sent email with ID: " + message.getId());
                return message;
        }
}
