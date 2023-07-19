package com.basak.dalcom.config;

import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NCPConfig {

    @Value("${cloud.ncp.clova-speech.callback-url-template}")
    public String CALLBACK_URL_TEMPLATE;

    public URL getClovaSpeechCallbackUrl(Integer presentationId, Integer speechId) {
        try {
            return new URL(String.format(CALLBACK_URL_TEMPLATE, presentationId, speechId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
