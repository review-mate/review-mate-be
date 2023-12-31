package com.somartreview.reviewmate.service.review;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.somartreview.reviewmate.domain.review.Review;
import com.somartreview.reviewmate.domain.review.image.ReviewImage;
import com.somartreview.reviewmate.domain.review.image.ReviewImageRepository;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.somartreview.reviewmate.exception.ErrorCode.AWS_S3_CLIENT_ERROR;
import static com.somartreview.reviewmate.exception.ErrorCode.REVIEW_IMAGE_FILE_IO_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewImageService {
    public static String CDN_DOMAIN = "image.reviewmate.co.kr";

    private final ReviewImageRepository reviewImageRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String s3ImageBucketName;


    @Async
    @Retryable(
            value = {ExternalServiceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 10000)
    )
    public void createAll(List<MultipartFile> reviewImageFiles, Review review) {
        for (MultipartFile reviewImageFile : reviewImageFiles) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .fileName(uploadReviewImageFilesOnS3(reviewImageFile))
                    .review(review)
                    .build();

            log.info(reviewImage.toString());
            reviewImage = reviewImageRepository.save(reviewImage);
            review.addReviewImage(reviewImage);
        }
    }

    private String uploadReviewImageFilesOnS3(MultipartFile reviewImage) {
        try {
            String fileName = System.currentTimeMillis() + "_" + reviewImage.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(reviewImage.getContentType());
            metadata.setContentLength(reviewImage.getSize());

            amazonS3Client.putObject(s3ImageBucketName, fileName, reviewImage.getInputStream(), metadata);
            return fileName;

        } catch (SdkClientException e) {
            throw new ExternalServiceException(AWS_S3_CLIENT_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DomainLogicException(REVIEW_IMAGE_FILE_IO_ERROR);
        }
    }

    public void removeReviewImageFiles(List<ReviewImage> reviewImages) {
        for (ReviewImage reviewImage : reviewImages) {
            removeReviewImageFilesOnS3(reviewImage.getFileName());
        }
    }

    private void removeReviewImageFilesOnS3(String fileName) {
        try {
            amazonS3Client.deleteObject(s3ImageBucketName, fileName);

        } catch (SdkClientException e) {
            throw new ExternalServiceException(AWS_S3_CLIENT_ERROR);
        }
    }
}
