package com.somartreview.reviewmate.domain.review;

import com.somartreview.reviewmate.domain.BaseEntity;
import com.somartreview.reviewmate.domain.reservation.Reservation;
import com.somartreview.reviewmate.domain.review.image.ReviewImage;
import com.somartreview.reviewmate.domain.review.tag.ReviewTag;
import com.somartreview.reviewmate.dto.review.ReviewUpdateRequest;
import com.somartreview.reviewmate.exception.DomainLogicException;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.somartreview.reviewmate.domain.review.ReviewPolarity.*;
import static com.somartreview.reviewmate.exception.ErrorCode.*;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_reservation_id_created_at", columnList = "reservation_id asc, createdAt DESC")
})
public class Review extends BaseEntity{

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_CONTENT_LENGTH = 255;
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "polarity")
    @Enumerated(EnumType.STRING)
    private ReviewPolarity polarity = NEUTRAL;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @OneToMany(mappedBy = "review")
    private List<ReviewTag> reviewTags = new ArrayList<>();

    @Column(nullable = false)
    private Long positiveTagsCount = 0L;

    @Column(nullable = false)
    private Long negativeTagsCount = 0L;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(Integer rating, String title, String content, final Reservation reservation) {
        validateRating(rating);
        this.rating = rating;
        validateTitle(title);
        this.title = title;
        validateContent(content);
        this.content = content;
        this.reservation = reservation;
    }

    private void validateRating(final Integer rating) {
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new DomainLogicException(REVIEW_RATING_ERROR);
        }
    }

    private void validateTitle(final String title) {
        if (title.isBlank() || title.length() > MAX_TITLE_LENGTH) {
            throw new DomainLogicException(REVIEW_TITLE_ERROR);
        }
    }

    private void validateContent(final String content) {
        if (content.isBlank() || content.length() > MAX_CONTENT_LENGTH) {
            throw new DomainLogicException(REVIEW_CONTENT_ERROR);
        }
    }

    public void addReviewTag(ReviewTag reviewTag) {
        this.reviewTags.add(reviewTag);

        if (reviewTag.getPolarity().equals(POSITIVE)) {
            positiveTagsCount++;
        }
        if (reviewTag.getPolarity().equals(NEGATIVE)) {
            negativeTagsCount++;
        }

        if (positiveTagsCount > negativeTagsCount) {
            this.polarity = POSITIVE;

        } else if (positiveTagsCount < negativeTagsCount) {
            this.polarity = NEGATIVE;

        } else {
            this.polarity = NEUTRAL;
        }
    }

    public void clearReviewTags() {
        this.reviewTags.clear();

        this.positiveTagsCount = 0L;
        this.negativeTagsCount = 0L;
        this.polarity = NEUTRAL;
    }

    public void addReviewImage(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
    }

    public void clearReviewImages() {
        this.reviewImages.clear();
    }

    public void updateReview(ReviewUpdateRequest reviewUpdateRequest) {
        validateRating(reviewUpdateRequest.getRating());
        this.rating = reviewUpdateRequest.getRating();
        validateTitle(reviewUpdateRequest.getTitle());
        this.title = reviewUpdateRequest.getTitle();
        validateContent(reviewUpdateRequest.getContent());
        this.content = reviewUpdateRequest.getContent();
    }
}
