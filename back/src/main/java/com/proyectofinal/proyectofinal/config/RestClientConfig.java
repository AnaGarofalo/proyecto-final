package com.proyectofinal.proyectofinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient whatsappRestClient(WhatsappProperties p) {
        String base = p.getGraph().getBaseUrl() + "/" + p.getGraph().getVersion();
        return RestClient.builder()
                .baseUrl(base)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + p.getAccessToken())
                .build();
    }
}
