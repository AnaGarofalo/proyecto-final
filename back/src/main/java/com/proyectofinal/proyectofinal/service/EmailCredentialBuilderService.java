package com.proyectofinal.proyectofinal.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

@Service
public class EmailCredentialBuilderService {
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${gmail.credentials.client-id}")
    private String clientId;
    @Value("${gmail.credentials.client-secret}")
    private String clientSecret;
    @Value("${gmail.auth.uri.google}")
    private String AuthUriGoogle;
    @Value("${gmail.token.uri.google}")
    private String tokenUriGoogle;
    // @Value("${gmail.auth.provider.X509Cert.url}")
    // private String gmailAuthProviderX509CertUrl;
    @Value("${gmail.redirect.uris}")
    private String gmailRedirectUris;

    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
                .setInstalled(new GoogleClientSecrets.Details()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setAuthUri(AuthUriGoogle)
                        .setTokenUri(tokenUriGoogle)
                        // .setAuthProviderX509CertUrl(gmailAuthProviderX509CertUrl)
                        .setRedirectUris(List.of(gmailRedirectUris)));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
    }
}
