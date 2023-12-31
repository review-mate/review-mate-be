package com.somartreview.reviewmate.dto.review;

import com.somartreview.reviewmate.domain.review.Review;
import com.somartreview.reviewmate.domain.review.ReviewPolarity;
import com.somartreview.reviewmate.domain.review.ReviewProperty;
import com.somartreview.reviewmate.domain.review.tag.ReviewTag;
import com.somartreview.reviewmate.service.review.ReviewTagsPredicate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

import static com.somartreview.reviewmate.service.review.ReviewImageService.CDN_DOMAIN;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WidgetReviewResponse {

    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @Schema(description = "리뷰 평점 (1 ~ 5)", example = "5")
    private Integer rating;

    @Schema(description = "리뷰 제목", example = "최고의 펜션")
    private String title;

    @Schema(description = "리뷰 내용", example = "바다뷰도 끝내주고, 너무 행복한 시간이였어요. 다음에 또 오고 싶어요.")
    private String content;

    @Schema(description = "리뷰를 단 고객의 이름", example = "권순찬")
    private String authorName;

    @Schema(description = "업로드 날짜", example = "2021.01.01")
    private String createdAt;

    @Schema(description = "리뷰 사진 URL", example = "https://image.reviewmate.co.kr/image.png")
    private List<String> reviewImageUrls;

    @Schema(description = "긍부정", example = "POSITIVE")
    private ReviewPolarity polarity;

    @Schema(description = "적용된 속성 혹은 키워드가 포함된 문자열의 인덱스들")
    private List<ReviewTagIndexResponse> reviewTagIndexResponses;

    public WidgetReviewResponse(final Review review, ReviewProperty property, String keyword) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.authorName = review.getReservation().getCustomer().getName();
        this.createdAt = review.getCreatedAt().toString();
        this.reviewImageUrls = review.getReviewImages().stream().map(reviewImage -> CDN_DOMAIN + "/" + reviewImage.getFileName()).toList();
        this.polarity = review.getPolarity();
        List<Predicate<ReviewTag>> reviewTagsPredicates = new ReviewTagsPredicate(property, keyword).getPredicates();
        this.reviewTagIndexResponses = review.getReviewTags().stream().filter(reviewTagsPredicates.stream().reduce(x -> true, Predicate::and)).map(ReviewTagIndexResponse::new).toList();
    }

    public WidgetReviewResponse(final Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.authorName = review.getReservation().getCustomer().getName();
        this.createdAt = review.getCreatedAt().toString();
        this.reviewImageUrls = review.getReviewImages().stream().map(reviewImage -> CDN_DOMAIN + "/" + reviewImage.getFileName()).toList();
        this.polarity = review.getPolarity();
        this.reviewTagIndexResponses = review.getReviewTags().stream().map(ReviewTagIndexResponse::new).toList();
    }
}