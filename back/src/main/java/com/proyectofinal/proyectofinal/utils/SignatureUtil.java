package com.proyectofinal.proyectofinal.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class SignatureUtil {
    private SignatureUtil(){}

    public static boolean isValidHmacSha256(String header, String body, String appSecret) {
        try {
            if (header == null || !header.startsWith("sha256=")) return false;
            String expected = header.substring(7);
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hmac = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
            String calc = toHex(hmac);
            return slowEquals(expected, calc);
        } catch (Exception e) {
            return false;
        }
    }
    private static String toHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
    private static boolean slowEquals(String a, String b) {
        if (a.length()!=b.length()) return false;
        int r=0;
        for (int i=0;i<a.length();i++) r |= a.charAt(i)^b.charAt(i);
        return r==0;
    }
}
