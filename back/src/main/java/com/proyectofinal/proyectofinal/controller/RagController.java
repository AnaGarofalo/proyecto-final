package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.service.RagService;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public IngestResponse ingest(@RequestPart("files") MultipartFile[] files) throws Exception {
        int count = ragService.ingestFiles(files);
        var resp = new IngestResponse();
        resp.setIndexed(count);
        return resp;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest req) {
        String answer = ragService.ask(req.getQuestion());
        List<String> sources = ragService.topSources(req.getQuestion());
        var resp = new ChatResponse();
        resp.setAnswer(answer);
        resp.setSources(sources);
        return resp;
    }

    @Data
    public static class ChatRequest {
        private String question;
    }

    @Data
    public static class ChatResponse {
        private String answer;
        private List<String> sources;
    }

    @Data
    public static class IngestResponse {
        private int indexed;
    }
}