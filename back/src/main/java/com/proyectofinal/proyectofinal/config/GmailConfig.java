package com.proyectofinal.proyectofinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyectofinal.proyectofinal.service.EmailCredentialBuilderService;

@Configuration
public class GmailConfig {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Autowired
    private EmailCredentialBuilderService emailCredentialBuilderService;

    @Bean
    public NetHttpTransport netHttpTransport() throws Exception {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public Gmail gmail(NetHttpTransport transport) throws Exception {
        return new Gmail.Builder(transport, JSON_FACTORY, 
                emailCredentialBuilderService.getCredentials(transport))
                .setApplicationName("Proyecto Final - Grupo 1")
                .build();
    }
}
