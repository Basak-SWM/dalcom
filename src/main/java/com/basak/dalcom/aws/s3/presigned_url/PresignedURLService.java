package com.basak.dalcom.aws.s3.presigned_url;

public interface PresignedURLService {

    String getPresignedURLForUpload(String bucketName, String objectKey);

    String getPresignedURLForUpload(String objectKey);

    String getPresignedURLForDownload(String bucketName, String objectKey);

    String getPresignedURLForDownload(String objectKey);
}
