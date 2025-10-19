package com.proyectofinal.proyectofinal.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.gmail.model.Message;

public class EmailMessageBuilder {
    public static Message Build(String to, String from, String subject, String bodyText) throws UnsupportedEncodingException {
        String emailBody = createRawEmailString(to, from, subject, bodyText);
        byte[] emailBytes = emailBody.getBytes("UTF-8");
        String encodedEmail = Base64UrlSafeEncoder.encode(emailBytes);
        return new Message().setRaw(encodedEmail);
    }

    private static String createRawEmailString(String to, String from, String subject, String bodyText) {
        List<String> headers = new ArrayList<>();
        headers.add("To: " + to);
        headers.add("From: " + from);
        headers.add("Subject: " + subject);
        headers.add("Content-Type: text/plain; charset=\"UTF-8\"");
        headers.add("MIME-Version: 1.0");
        String headerString = String.join("\r\n", headers);
        return headerString + "\r\n\r\n" + bodyText;
    }
}
