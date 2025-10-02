package com.proyectofinal.proyectofinal.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    public ChatLanguageModel chatModel(
            @Value("${openai.api-key:${OPENAI_API_KEY:}}") String apiKey,
            @Value("${openai.chat-model:gpt-4o-mini}") String model
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Falta OPENAI_API_KEY (variable de entorno o application.properties)");
        }
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .temperature(0.1)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel(
            @Value("${openai.api-key:${OPENAI_API_KEY:}}") String apiKey,
            @Value("${openai.embedding-model:text-embedding-3-small}") String model
    ) {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
}
