package com.basak.dalcom.external_api.common.service;

import com.basak.dalcom.config.WasakConfig;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@Service
class APIServiceImpl<T> implements APIService<T> {

    public final static String URL_PREFIX = "/api";

    private final RestTemplate restTemplate;
    private final WasakConfig wasakConfig;

    private ParameterizedTypeReference<T> getTypeReference() {
        return new ParameterizedTypeReference<T>() {
        };
    }

    private ResponseEntity<T> exchange(String endpoint, HttpMethod method,
        HttpEntity<?> requestEntity) throws MalformedURLException {
        ResponseEntity<T> responseEntity = restTemplate.exchange(
            endpoint, method, requestEntity, getTypeReference());

        return responseEntity;
    }

    public ResponseEntity<T> getResource(String path, QueryParams queryParams, HttpHeaders headers)
        throws MalformedURLException {
        URL apiEndpoint = wasakConfig.getWasakAPIEndpoint(URL_PREFIX + path);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiEndpoint.toString())
            .queryParams(queryParams.toMultiValueMap());
        URL buildEndpoint = uriBuilder.build().toUri().toURL();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        return exchange(buildEndpoint.toString(), HttpMethod.GET, requestEntity);
    }

    public ResponseEntity<T> createResource(String path, Object requestBody, HttpHeaders headers)
        throws MalformedURLException {
        URL apiEndpoint = wasakConfig.getWasakAPIEndpoint(URL_PREFIX + path);
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);

        return exchange(apiEndpoint.toString(), HttpMethod.POST, requestEntity);
    }

    public ResponseEntity<T> updateResource(String path, Object requestBody, HttpHeaders headers)
        throws MalformedURLException {
        URL apiEndpoint = wasakConfig.getWasakAPIEndpoint(URL_PREFIX + path);

        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
        return exchange(apiEndpoint.toString(), HttpMethod.PATCH, requestEntity);
    }

    public ResponseEntity<T> deleteResource(String path, HttpHeaders headers)
        throws MalformedURLException {
        URL apiEndpoint = wasakConfig.getWasakAPIEndpoint(URL_PREFIX + path);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(apiEndpoint.toString(), HttpMethod.DELETE, requestEntity);
    }
}
