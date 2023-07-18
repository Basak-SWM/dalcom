package com.basak.dalcom.external_api.common.service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class QueryParams {

    private final MultiValueMap<String, String> mmMap = new LinkedMultiValueMap<>();

    public void addQueryParam(String name, String value) {
        mmMap.add(name, value);
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        return mmMap;
    }
}
