package com.basak.dalcom.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.basak.dalcom.config.aws.S3Config;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class S3Service {

    private final S3Config config;
    private final AmazonS3 amazonS3;

    public static String getKeyFromUrl(String stringUrl) {
        String[] split = stringUrl.split("amazonaws.com/");
        String key = split[1];
        return key;
    }

    public boolean isExist(String key) {
        return isExist(config.DEFAULT_BUCKET_NAME, key);
    }

    public boolean isExist(String bucketName, String key) {
        return amazonS3.doesObjectExist(bucketName, key);
    }

    public void uploadAsJson(String key, String body) {
        amazonS3.putObject(config.DEFAULT_BUCKET_NAME, key, body);
    }

    public S3Object download(String key) {
        S3Object result = amazonS3.getObject(config.DEFAULT_BUCKET_NAME, key);
        return result;
    }

    public void deleteByKey(String key) {
        amazonS3.deleteObject(config.DEFAULT_BUCKET_NAME, key);
    }
}
