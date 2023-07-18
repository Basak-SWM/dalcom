package com.basak.dalcom.external_api.common.service;

import java.net.MalformedURLException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface APIService<T> {

    private ParameterizedTypeReference<T> getTypeReference() {
        return new ParameterizedTypeReference<T>() {
        };
    }

    ResponseEntity<T> getResource(String path, QueryParams queryParams, HttpHeaders headers)
        throws MalformedURLException;

    ResponseEntity<T> createResource(String path, Object requestBody, HttpHeaders headers)
        throws MalformedURLException;

    ResponseEntity<T> updateResource(String path, Object requestBody, HttpHeaders headers)
        throws MalformedURLException;

    ResponseEntity<T> deleteResource(String path, HttpHeaders headers)
        throws MalformedURLException;
}
