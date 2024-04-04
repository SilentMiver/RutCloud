package com.example.rutcloud.services;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;
    // Пока опасно. Не использовать

    public void uploadFile(String bucketName, String objectName, String filePath) throws Exception {
        try {

            ObjectWriteResponse response = minioClient.uploadObject(
                   UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            System.out.println("File uploaded successfully. Object ETag: " + response.etag());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }
}
