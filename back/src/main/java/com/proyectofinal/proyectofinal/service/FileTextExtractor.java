package com.proyectofinal.proyectofinal.service;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class FileTextExtractor {

    private final AutoDetectParser parser = new AutoDetectParser();

    public String extract(byte[] bytes, String filename) throws Exception {
        BodyContentHandler handler = new BodyContentHandler(-1); // sin límite
        Metadata metadata = new Metadata();
    if (filename != null) metadata.set("resourceName", filename);
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            parser.parse(stream, handler, metadata, new ParseContext());
            return handler.toString();
        }
    }
}