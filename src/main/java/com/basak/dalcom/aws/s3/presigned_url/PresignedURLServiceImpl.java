package com.basak.dalcom.aws.s3.presigned_url;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.basak.dalcom.config.aws.S3Config;
import java.net.URL;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PresignedURLServiceImpl implements PresignedURLService {

    private final S3Config config;

    private final AmazonS3 amazonS3;

    private String getPresignedURL(String bucketName, String objectKey, HttpMethod method) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedURLRequest(
            bucketName, objectKey, method);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    @Override
    public String getPresignedURLForUpload(String bucketName, String objectKey) {
        return getPresignedURL(bucketName, objectKey, HttpMethod.PUT);
    }

    @Override
    public String getPresignedURLForUpload(String objectKey) {
        return getPresignedURLForUpload(config.DEFAULT_BUCKET_NAME, objectKey);
    }

    @Override
    public String getPresignedURLForDownload(String bucketName, String objectKey) {
        return getPresignedURL(bucketName, objectKey, HttpMethod.GET);
    }

    @Override
    public String getPresignedURLForDownload(String objectKey) {
        return getPresignedURLForDownload(config.DEFAULT_BUCKET_NAME, objectKey);
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
