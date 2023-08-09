package com.basak.dalcom.config;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        CloseableHttpClient client = HttpClientBuilder
            .create()
            .setMaxConnTotal(50)
            .setMaxConnPerRoute(20)
            .build();

        factory.setHttpClient(client);
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(60000);

        RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(factory));
        restTemplate.setInterceptors(
            Collections.singletonList(new RequestResponseLoggingInterceptor()));

        return restTemplate;
    }
}

class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = Logger.getLogger(RestTemplateConfig.class.getName());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {
        describeRequest(request);
        ClientHttpResponse response = execution.execute(request, body);
        describeResponse(request, response);

        return response;
    }

    private void describeRequest(HttpRequest request) {
        logger.log(Level.INFO, "Request URI: {0}", request.getURI());
    }

    private void describeResponse(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        logger.log(Level.INFO, String.format("Response URI with status code %d: [%s] %s",
            response.getStatusCode().value(), request.getMethod(), request.getURI())
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.log(Level.INFO, "Error occurred: {0}", response.getBody().toString());
        }
    }
}