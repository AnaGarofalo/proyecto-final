package com.proyectofinal.proyectofinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestClientConfig {
    @Bean
    public WebClient whatsappWebClient(WhatsappProperties p) {
        String base = p.getGraph().getBaseUrl() + "/" + p.getGraph().getVersion();
        return WebClient.builder()
                .baseUrl(base)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + p.getAccessToken())
                .build();
    }
}
