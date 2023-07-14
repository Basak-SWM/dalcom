package com.basak.dalcom.domain.test;

import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@AllArgsConstructor
class UrlDto {

    private final String url;
}

@Getter
@AllArgsConstructor
class ThreePrefixObjectKeyDto {

    private final String prefix1;
    private final String prefix2;
    private final String prefix3;
    private final String extension;

    public String getObjectKey() {
        LocalDateTime now = LocalDateTime.now();
        Long milliseconds = now.toInstant(ZoneOffset.ofHours(9)).toEpochMilli();
        UUID uuid = UUID.randomUUID();
        String objectKey = milliseconds + "_" + uuid + "." + extension;

        return prefix1 + "/" + prefix2 + "/" + prefix3 + "/" + objectKey;
    }
}

@Getter
@AllArgsConstructor
class DownloadRequestDto {

    private final String prefix1;
    private final String prefix2;
    private final String prefix3;

    public String getPrefix() {
        return prefix1 + "/" + prefix2 + "/" + prefix3 + "/";
    }
}

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final PresignedURLService presignedURLService;

    @PostMapping("/presigned-url/upload")
    public UrlDto createUploadPresignedUrl(@RequestBody ThreePrefixObjectKeyDto dto) {
        String objectKey = dto.getObjectKey();
        URL createdUrl = presignedURLService.getPresignedURLForUpload(objectKey);
        return new UrlDto(createdUrl.toString());
    }

    @GetMapping("/presigned-url/download/{prefix1}/{prefix2}/{prefix3}")
    public List<URL> getDownloadPresignedUrl(DownloadRequestDto dto) {
        return presignedURLService.getPresignedURLsForDownload(dto.getPrefix());
    }
}
