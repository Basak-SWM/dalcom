package com.basak.dalcom.domain.core.speech.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {

    @Schema(description = "URL link", type = "string", example = "https://example-bucket.s3.ap-northeast-2.amazonaws.com/1/1/148_43cc08a0-986d-45e2-b4bc-a5775ce73ecc.mp3")
    private URL url;

    public UrlDto(String stringUrl) throws MalformedURLException {
        this.url = convertToUrl(stringUrl);
    }

    private URL convertToUrl(String stringUrl) throws MalformedURLException {
        return new URL(stringUrl);
    }


}
