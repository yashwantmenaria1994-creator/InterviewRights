package com.example.interviewrights.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.serviceImpl.S3Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest;
import software.amazon.awssdk.services.rekognition.model.CompareFacesResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;
@Service
@RequiredArgsConstructor
public class FaceVerificationService {

    @Autowired
    private final UserRepository repo;

    @Autowired
    private final S3Service s3Service;

    private final RekognitionClient rekognitionClient;

    /**
     * Candidate की saved profile photo और live webcam photo compare करता है.
     * Similarity 90% या उससे अधिक होने पर true return करता है.
     */
    public Map<String, Object> verifyFace(String candidateEmail, MultipartFile livePhoto) {

        // Candidate fetch
        Optional<User> findByEmail = repo.findByEmail(candidateEmail);
        
        if(!findByEmail.isPresent()) {
        	
        }
        User user = findByEmail.get();
        
        // Saved profile photo URL (already stored in DB)
        String profileImageUrl = user.getProfilePic();

        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            throw new RuntimeException("Profile photo not found");
        }

        // Live photo upload to S3
        String liveImageUrl = s3Service.uploadFile(livePhoto);

        // Compare faces
        Map<String, Object> similarity = compareFaces(profileImageUrl, liveImageUrl);

        // Optional: temporary uploaded live image delete
        // s3Service.deleteFile(liveImageUrl);

        return similarity;
        
        
    }

    /**
     * AWS Rekognition CompareFaces API call.
     * Source = Profile Photo
     * Target = Live Photo
     */
    public Map<String, Object> compareFaces(String sourceUrl, String targetUrl) {

        // URL से bucket और key निकालें
        S3Location source = parseS3Url(sourceUrl);
        S3Location target = parseS3Url(targetUrl);

        // Source image
        Image sourceImage = Image.builder()
                .s3Object(S3Object.builder()
                        .bucket(source.bucket())
                        .name(source.key())
                        .build())
                .build();

        // Target image
        Image targetImage = Image.builder()
                .s3Object(S3Object.builder()
                        .bucket(target.bucket())
                        .name(target.key())
                        .build())
                .build();

        // Compare request
        if (sourceImage == null) {
            throw new RuntimeException("Source image is null");
        }

        if (targetImage == null) {
            throw new RuntimeException("Target image is null");
        }
        
        CompareFacesRequest request = CompareFacesRequest.builder()
                .sourceImage(sourceImage)
                .targetImage(targetImage)
                .similarityThreshold(80f) // AWS minimum threshold
                .build();

        // API call
        CompareFacesResponse response =
                rekognitionClient.compareFaces(request);

        Double similarity = 0.0;
        boolean matched = false;
        
        // Match found
        if (!response.faceMatches().isEmpty()) {
            similarity = response.faceMatches()
                    .get(0)
                    .similarity().doubleValue();

            matched = similarity >= 80F;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("matched", matched);
        result.put("similarity", similarity);
        System.out.println("Final Result: " + result);

        return result;
    }
    

    /**
     * S3 URL को bucket और key में convert करता है.
     *
     * Example:
     * https://my-bucket.s3.ap-south-1.amazonaws.com/profile/test.jpg
     */
    private S3Location parseS3Url(String fileUrl) {
        try {
            URI uri = URI.create(fileUrl);

            // Host: my-bucket.s3.ap-south-1.amazonaws.com
            String host = uri.getHost();
            String bucket = host.split("\\.")[0];

            // Path: /profile/test.jpg
            String key = uri.getPath().substring(1);

            return new S3Location(bucket, key);
        } catch (Exception e) {
            throw new RuntimeException("Invalid S3 URL: " + fileUrl, e);
        }
    }

    /**
     * Bucket और key रखने के लिए helper record
     */
    private record S3Location(String bucket, String key) {
    }
}