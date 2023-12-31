package com.basak.dalcom.external_api.wasak.service;

import com.basak.dalcom.config.WasakConfig;
import com.basak.dalcom.domain.common.exception.HandledException;
import com.basak.dalcom.domain.common.exception.UnhandledException;
import com.basak.dalcom.external_api.common.service.APIServiceImpl;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WasakService extends APIServiceImpl {

    private final WasakConfig wasakConfig;

    public WasakService(RestTemplate restTemplate, WasakConfig wasakConfig) {
        super(restTemplate);
        this.wasakConfig = wasakConfig;
    }

    @Override
    protected URL getUrl(String path) {
        return wasakConfig.getWasakAPIEndpoint(path);
    }

    @SneakyThrows
    public void requestAnalysis1(Integer presentationId, Integer speechId,
        URL callbackUrl, String uploadKey, URL downloadUrl) {
        Map<String, String> body = new HashMap<>();
        body.put("callback_url", callbackUrl.toString());
        body.put("upload_key", uploadKey);
        body.put("download_url", downloadUrl.toString());

        try {
            ResponseEntity<Void> response = createResource(
                "/v1/presentations/" + presentationId + "/speech/" + speechId + "/analysis-1", body,
                null);

            if (response.getStatusCode().isError()) {
                throw new HandledException(response.getStatusCode(),
                    "Something went wrong in wasak.");
            }
        } catch (MalformedURLException ex) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Malformed URL error."
            );
        }
    }

    @SneakyThrows
    public void requestAnalysis2(Integer presentationId, Integer speechId) {
        Map<String, Integer> body = new HashMap<>();
        body.put("presentation_id", presentationId);
        body.put("speech_id", speechId);

        try {
            ResponseEntity<Void> response = createResource(
                "/v1/presentations/" + presentationId + "/speech/" + speechId + "/analysis-2", body,
                null);
            if (response.getStatusCode().isError()) {
                throw new HandledException(response.getStatusCode(),
                    "Something went wrong in wasak.");
            }
        } catch (MalformedURLException ex) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Malformed URL error."
            );
        }
    }
}
