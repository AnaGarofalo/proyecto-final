package com.proyectofinal.proyectofinal.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class OpenAIConfig {

    @Bean
    // Este objeto representa el modelo del chat (GPT-4o-mini)
    public ChatLanguageModel chatModel(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.chat.model}") String model) {
        if (StringUtils.isEmpty(apiKey)) {
            throw new IllegalStateException("Missing OPENAI_API_KEY");
        }
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .temperature(0.1)
                .build();
    }

    @Bean
    // Este objeto representa el modelo de embeddings (text-embedding-3-small)
    public EmbeddingModel embeddingModel(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.embedding.model}") String model) {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .build();
    }

    @Bean
    // Este objeto representa el almacenamiento de los embeddings
    public static EmbeddingStore<TextSegment> createEmbeddingStore(
            @Value("${database.name.rag}") String database,
            @Value("${database.port}") Integer port,
            @Value("${database.domain}") String domain,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String password
    ) {
        return PgVectorEmbeddingStore.builder()
                .host(domain)
                .port(port) // puerto que estás usando
                .database(database)
                .user(user)
                .password(password)
                .table("embeddings")
                .createTable(true) // Crear tabla embeddings si no existe
                .dropTableFirst(false) // Eliminar y recrear tabla embeddings
                .useIndex(true)
                .indexListSize(100)
                .dimension(1536) // la dimensión de tu modelo de embeddings
                .build();
    }

    @Bean
    // Este objeto representa la conexión a la bdd de rag
    public JdbcTemplate embeddingJdbcTemplate(
            @Value("${database.domain}") String domain,
            @Value("${database.port}") Integer port,
            @Value("${database.name.rag}") String database,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String password
    ) {
        String url = String.format("jdbc:postgresql://%s:%d/%s", domain, port, database);
        DataSource dataSource = DataSourceBuilder.create()
                .url(url)
                .username(user)
                .password(password)
                .build();
        return new JdbcTemplate(dataSource);
    }
}
