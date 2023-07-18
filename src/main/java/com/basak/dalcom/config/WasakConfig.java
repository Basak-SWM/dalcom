package com.basak.dalcom.config;

import java.net.URL;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class WasakConfig {

    private final String API_HOST;

    public WasakConfig(
        @Value("${wasak.api.host}") String host) {
        this.API_HOST = host;
    }

    public URL getWasakAPIEndpoint(String path) {
        try {
            return new URL(API_HOST + path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
