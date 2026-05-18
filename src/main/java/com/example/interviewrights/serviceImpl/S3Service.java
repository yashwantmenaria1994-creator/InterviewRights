package com.example.interviewrights.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.bucket-name}")
    private String bucketName;
    
	@Value("${aws.region}")
	private String region;

    public String uploadFile(MultipartFile file) {
        try {
            // 1. Validation
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // 2. Safe filename
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                originalName = "file";
            }

            // Spaces replace
            originalName = originalName.replaceAll("\\s+", "_");

            // 3. Unique key
            String key = "profile/" + UUID.randomUUID() + "_" + originalName;

            // 4. Upload request
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(
                            file.getContentType() != null
                                    ? file.getContentType()
                                    : "application/octet-stream"
                    )
                    .build();

            // 5. Upload
            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

            // 6. Return URL
            return "https://" + bucketName
                    + ".s3." + region
                    + ".amazonaws.com/"
                    + key;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("S3 Upload Failed: " + e.getMessage(), e);
        }
    }
}