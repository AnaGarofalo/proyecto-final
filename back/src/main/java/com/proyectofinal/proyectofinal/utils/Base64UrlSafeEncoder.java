package com.proyectofinal.proyectofinal.utils;

import com.google.api.client.util.Base64;

public class Base64UrlSafeEncoder {
    public static String encode(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }
}