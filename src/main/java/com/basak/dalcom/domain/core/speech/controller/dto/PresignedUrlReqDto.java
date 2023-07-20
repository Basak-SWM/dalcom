package com.basak.dalcom.domain.core.speech.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlReqDto {

    @Schema(anyOf = AvailableExtension.class, description = "업로드할 파일의 확장자", implementation = AvailableExtension.class)
    private AvailableExtension extension;

    @Getter
    @AllArgsConstructor
    public enum AvailableExtension {
        webm("webm"),
        mp3("mp3");

        private final String value;

        public static Optional<AvailableExtension> enumOf(String s) {
            return Arrays.stream(AvailableExtension.values()).filter(r -> r.value.equals(s))
                .findAny();
        }

        public static Optional<String> valueOf(AvailableExtension ext) {
            return Arrays.stream(AvailableExtension.values()).filter(r -> r.equals(ext))
                .map(r -> r.value)
                .findAny();
        }
    }


}
