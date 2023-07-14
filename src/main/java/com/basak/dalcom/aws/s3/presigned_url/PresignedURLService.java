package com.basak.dalcom.aws.s3.presigned_url;

import java.net.URL;
import java.util.List;

public interface PresignedURLService {

    URL getPresignedURLForUpload(String bucketName, String objectKey);

    URL getPresignedURLForUpload(String objectKey);

    URL getPresignedURLForDownload(String bucketName, String objectKey);

    URL getPresignedURLForDownload(String objectKey);

    List<URL> getPresignedURLsForDownload(String bucketName, String prefix);

    List<URL> getPresignedURLsForDownload(String prefix);
}
