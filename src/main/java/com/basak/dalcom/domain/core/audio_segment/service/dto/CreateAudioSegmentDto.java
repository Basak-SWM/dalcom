package com.basak.dalcom.domain.core.audio_segment.service.dto;

import com.basak.dalcom.domain.common.exception.UnhandledException;
import java.net.URI;
import java.net.URL;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CreateAudioSegmentDto {

    private final Integer speechId;
    private final URL url;

    public CreateAudioSegmentDto(Integer speechId, URL url) {
        this.speechId = speechId;
        this.url = getQueryParamsRemovedUrl(url);
    }

    private URL getQueryParamsRemovedUrl(URL url) {
        try {
            URI uri = url.toURI();
            URI uriWithoutQuery = new URI(
                uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment());
            return uriWithoutQuery.toURL();
        } catch (Exception e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something goes wrong in CreateAudioSegmentDto.removeQueryParams : "
                    + e.getMessage());
        }
    }
}
