package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.app_user.ChatRequestDTO;
import com.proyectofinal.proyectofinal.dto.app_user.ChatResponseDTO;
import com.proyectofinal.proyectofinal.dto.app_user.IngestResponseDTO;
import com.proyectofinal.proyectofinal.service.RagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    // Subida de archivos a la base de datos de vectores
    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IngestResponseDTO> ingest(@RequestPart("files") MultipartFile[] files) throws Exception {
        IngestResponseDTO resp = ragService.ingestFiles(files);

        return ResponseEntity.ok(resp);
    }

    // Preguntas al modelo, es solo para pruebas y luego será removido
    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(@RequestBody ChatRequestDTO req) {
        String answer = ragService.ask(req.getQuestion());
        List<String> sources = ragService.topSources(req.getQuestion());

        ChatResponseDTO resp = ChatResponseDTO.builder()
                .answer(answer)
                .sources(sources)
                .build();

        return ResponseEntity.ok(resp);
    }

}