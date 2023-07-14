package com.basak.dalcom.aws.s3.presigned_url;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.basak.dalcom.config.aws.S3Config;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PresignedURLServiceImpl implements PresignedURLService {

    private final S3Config config;

    private final AmazonS3 amazonS3;

    private URL getPresignedURL(String bucketName, String objectKey, HttpMethod method) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedURLRequest(
            bucketName, objectKey, method);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    @Override
    public URL getPresignedURLForUpload(String bucketName, String objectKey) {
        return getPresignedURL(bucketName, objectKey, HttpMethod.PUT);
    }

    @Override
    public URL getPresignedURLForUpload(String objectKey) {
        return getPresignedURLForUpload(config.DEFAULT_BUCKET_NAME, objectKey);
    }

    @Override
    public URL getPresignedURLForDownload(String bucketName, String objectKey) {
        return getPresignedURL(bucketName, objectKey, HttpMethod.GET);
    }

    @Override
    public URL getPresignedURLForDownload(String objectKey) {
        return getPresignedURLForDownload(config.DEFAULT_BUCKET_NAME, objectKey);
    }

    @Override
    public List<URL> getPresignedURLsForDownload(String bucketName, String prefix) {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName, prefix);

        Date expiration = getPresignedUrlExpirationAfterMin(config.EXPIRATION_TIME_MIN);

        return result.getObjectSummaries().stream()
            .filter(o -> o.getSize() > 0) // 디렉토리는 제외
            .sorted(Comparator.comparing(S3ObjectSummary::getKey)) // key를 기준으로 정렬
            .map(o -> amazonS3.generatePresignedUrl(bucketName, o.getKey(), expiration)) // URL 생성
            .toList();
    }

    @Override
    public List<URL> getPresignedURLsForDownload(String prefix) {
        return getPresignedURLsForDownload(config.DEFAULT_BUCKET_NAME, prefix);
    }

    private GeneratePresignedUrlRequest getGeneratePresignedURLRequest(
        String bucketName, String objectKey, HttpMethod method) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey)
            .withMethod(method)
            .withExpiration(getPresignedUrlExpirationAfterMin(config.EXPIRATION_TIME_MIN));

        request.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString()
        );

        return request;
    }


    private Long convertExpirationMinuteToMillis(Integer expirationTimeMin) {
        return expirationTimeMin * 60 * 1000L;
    }

    private Date getPresignedUrlExpirationAfterMin(Integer expirationMinute) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        expTimeMillis += convertExpirationMinuteToMillis(expirationMinute);
        expiration.setTime(expTimeMillis);

        return expiration;
    }
}
