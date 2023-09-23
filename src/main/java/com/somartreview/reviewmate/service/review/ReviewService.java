package com.somartreview.reviewmate.service.review;

import com.somartreview.reviewmate.domain.product.SingleTravelProduct;
import com.somartreview.reviewmate.domain.reservation.Reservation;
import com.somartreview.reviewmate.domain.review.*;
import com.somartreview.reviewmate.dto.review.*;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.service.ReservationService;
import com.somartreview.reviewmate.service.products.SingleTravelProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.ArrayList;
import java.util.List;

import static com.somartreview.reviewmate.exception.ErrorCode.*;
import static com.somartreview.reviewmate.exception.ErrorCode.REVIEW_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewTagService reviewTagService;
    private final ReservationService reservationService;
    private final SingleTravelProductService singleTravelProductService;

    @Transactional
    public Long create(String partnerDomain, String reservationPartnerCustomId, ReviewCreateRequest request, List<MultipartFile> reviewImageFiles) {
        final Reservation reservation = reservationService.findByPartnerDomainAndPartnerCustomId(partnerDomain, reservationPartnerCustomId);
        Review review = request.toEntity(reservation);
        reviewRepository.save(review);

        reservation.getTravelProduct().updateReviewData(review.getRating());
        reservation.addReview(review);

        if (reviewImageFiles != null) {
            List<ReviewImage> reviewImages = createReviewImages(reviewImageFiles);
            review.appendReviewImage(reviewImages);
        }

        // Impl Requesting review inference through API gateway
        // Impl Requesting review inference through kafka

        return review.getId();
    }

    private void validateExistReviewByReservationId(Long reservationId) {
        if (reviewRepository.existsByReservation_Id(reservationId))
            throw new DomainLogicException(REVIEW_ALREADY_EXISTS_ON_RESERVATION);
    }

    private List<ReviewImage> createReviewImages(List<MultipartFile> reviewImageFiles) {
        return reviewImageFiles.stream()
                .map(reviewImageFile -> ReviewImage.builder()
                        .url(uploadReviewImageOnS3(reviewImageFile))
                        .build())
                .toList();
    }

    private String uploadReviewImageOnS3(MultipartFile reviewImage) {
        //  Impl uploading review image to S3 and get the url
        return "https://www.testThumbnailUrl.com";
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new DomainLogicException(REVIEW_NOT_FOUND));
    }

    public WidgetReviewResponse getWidgetReviewResponseById(Long id) {
        final Review review = findById(id);
        final List<ReviewTag> foundReviewTags = reviewTagService.findReviewTagsByReviewId(review.getId());

        return new WidgetReviewResponse(review, foundReviewTags);
    }

    public Page<WidgetReviewResponse> searchWidgetReviewResponsesWithPaging(String partnerDomain, String travelProductPartnerCustomId,
                                                                            ReviewProperty property, String keyword,
                                                                            ReviewOrderCriteria orderCriteria,
                                                                            Integer page, Integer size) {
        List<WidgetReviewResponse> widgetReviewResponses = new ArrayList<>();
        WidgetReviewSearchCond widgetReviewSearchCond = new WidgetReviewSearchCond(property, keyword, orderCriteria);
        Pageable pageable = PageRequest.of(page, size);

        Page<Review> foundReviews = reviewRepository.searchWidgetReviews(partnerDomain, travelProductPartnerCustomId, widgetReviewSearchCond, pageable);
        for (Review review : foundReviews) {
            List<ReviewTag> foundReviewTags = reviewTagService.findReviewTagsByReviewId(review.getId());
            widgetReviewResponses.add(new WidgetReviewResponse(review, foundReviewTags));
        }

        long totalCount = foundReviews.getTotalElements();

        return new PageImpl<>(widgetReviewResponses, pageable, totalCount);
    }

    public ProductReviewStatisticsResponse getReviewStatisticsResponses(String partnerDomain, String singleTravelProductPartnerCustomId) {
        final SingleTravelProduct singleTravelProduct = singleTravelProductService.findByPartnerDomainAndPartnerCustomId(partnerDomain, singleTravelProductPartnerCustomId);
        List<Long> ratingCounts = reviewRepository.countReviewRatingByTravelProductId(singleTravelProduct.getId());

        return ProductReviewStatisticsResponse.builder()
                .averageRating(singleTravelProduct.getRating())
                .reviewCount(singleTravelProduct.getReviewCount())
                .oneStarRatingCount(ratingCounts.get(0))
                .twoStarRatingCount(ratingCounts.get(1))
                .threeStarRatingCount(ratingCounts.get(2))
                .fourStarRatingCount(ratingCounts.get(3))
                .fiveStarRatingCount(ratingCounts.get(4))
                .build();
    }

    public List<ProductReviewTagStatisticsResponse> getProductReviewTagStatisticsResponses(String partnerDomain, String singleTravelProductPartnerCustomId) {
        long singleTravelProductId = singleTravelProductService.findByPartnerDomainAndPartnerCustomId(partnerDomain, singleTravelProductPartnerCustomId).getId();

        Map<ReviewProperty, ProductReviewTagStatisticsResponse> reviewTagStatisticsMap = new EnumMap<>(ReviewProperty.class);
        List<ReviewTagStatisticsDto> reviewTagStatisticsDtos = reviewRepository.findReviewTagStatisticsByTravelProductId(singleTravelProductId);

        for (ReviewTagStatisticsDto reviewTagStatisticDto : reviewTagStatisticsDtos) {
            if (reviewTagStatisticsMap.get(reviewTagStatisticDto.getProperty()) == null) {
                ProductReviewTagStatisticsResponse productReviewTagStatisticsResponse = new ProductReviewTagStatisticsResponse(reviewTagStatisticDto.getProperty());
                reviewTagStatisticsMap.put(reviewTagStatisticDto.getProperty(), productReviewTagStatisticsResponse);
            }

            if (reviewTagStatisticDto.getPolarity() == ReviewPolarity.POSITIVE) {
                reviewTagStatisticsMap.get(reviewTagStatisticDto.getProperty()).setPositiveCount(reviewTagStatisticDto.getCount());
            } else {
                reviewTagStatisticsMap.get(reviewTagStatisticDto.getProperty()).setNegativeCount(reviewTagStatisticDto.getCount());
            }
        }

        List<ProductReviewTagStatisticsResponse> reviewTagStatisticsResponses = new ArrayList<>(reviewTagStatisticsMap.values().stream().toList());
        reviewTagStatisticsResponses.sort(ReviewService::reviewTagCountDescComparator);

        return reviewTagStatisticsResponses;
    }

    private static int reviewTagCountDescComparator(ProductReviewTagStatisticsResponse o1, ProductReviewTagStatisticsResponse o2) {
        return -1 * Long.compare(o1.getPositiveCount() + o1.getNegativeCount(), o2.getPositiveCount() + o2.getNegativeCount());
    }

    @Transactional
    public void update(Long id, ReviewUpdateRequest request, List<MultipartFile> reviewImageFiles) {
        Review review = findById(id);

        review.getReservation().getTravelProduct().removeReviewData(review.getRating());
        review.clearReviewTags();
        review.clearReviewImages();

        review.updateReview(request);
        review.getReservation().getTravelProduct().updateReviewData(request.getRating());
        List<ReviewImage> reviewImages = createReviewImages(reviewImageFiles);
        review.appendReviewImage(reviewImages);

        // Impl Requesting review inference through API gateway
        // Impl Requesting review inference through kafka
    }
}
