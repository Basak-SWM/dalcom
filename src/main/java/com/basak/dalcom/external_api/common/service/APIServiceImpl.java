package com.basak.dalcom.external_api.common.service;

import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public abstract class APIServiceImpl<T> implements APIService<T> {

    protected final RestTemplate restTemplate;

    protected APIServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected abstract URL getUrl(String path);

    private ParameterizedTypeReference<T> getTypeReference() {
        return new ParameterizedTypeReference<T>() {
        };
    }

    private ResponseEntity<T> exchange(String endpoint, HttpMethod method,
        HttpEntity<?> requestEntity) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(
            endpoint, method, requestEntity, getTypeReference());

        return responseEntity;
    }

    public ResponseEntity<T> getResource(String path, QueryParams queryParams, HttpHeaders headers)
        throws MalformedURLException {
        URL apiEndpoint = getUrl(path);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiEndpoint.toString())
            .queryParams(queryParams.toMultiValueMap());
        URL buildEndpoint = uriBuilder.build().toUri().toURL();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        return exchange(buildEndpoint.toString(), HttpMethod.GET, requestEntity);
    }

    public ResponseEntity<T> createResource(String path, Object requestBody, HttpHeaders headers)
        throws MalformedURLException {
        URL apiEndpoint = getUrl(path);
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);

        return exchange(apiEndpoint.toString(), HttpMethod.POST, requestEntity);
    }

    public ResponseEntity<T> updateResource(String path, Object requestBody, HttpHeaders headers) {
        URL apiEndpoint = getUrl(path);

        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
        return exchange(apiEndpoint.toString(), HttpMethod.PATCH, requestEntity);
    }

    public ResponseEntity<T> deleteResource(String path, HttpHeaders headers) {
        URL apiEndpoint = getUrl(path);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(apiEndpoint.toString(), HttpMethod.DELETE, requestEntity);
    }
}
