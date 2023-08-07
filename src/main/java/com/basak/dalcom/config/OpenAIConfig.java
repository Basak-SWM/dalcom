package com.basak.dalcom.config;

import java.net.URL;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OpenAIConfig {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final String secretKey;

    public OpenAIConfig(
        @Value("${openai.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public URL getAPIEndpoint() {
        try {
            return new URL(OPENAI_API_URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
