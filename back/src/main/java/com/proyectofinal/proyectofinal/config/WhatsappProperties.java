package com.proyectofinal.proyectofinal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "whatsapp")
@Getter @Setter
public class WhatsappProperties {
    private Graph graph = new Graph();
    private Webhook webhook = new Webhook();
    private String verifyToken;
    private String accessToken;
    private App app = new App();
    private String wabaId;
    private String phoneNumberId;

    @Getter @Setter public static class Graph { private String baseUrl; private String version; }
    @Getter @Setter public static class Webhook { private String path; }
    @Getter @Setter public static class App { private String id; private String secret; }
}
