package com.example.codemate.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class GPTConfig {
    @Value("${chatgpt.api-key}")
    private String token;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(token, Duration.ofSeconds(60));
    }
}
