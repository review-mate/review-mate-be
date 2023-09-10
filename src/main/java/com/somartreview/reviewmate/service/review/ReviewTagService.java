package com.somartreview.reviewmate.service.review;

import com.somartreview.reviewmate.domain.review.ReviewTag;
import com.somartreview.reviewmate.domain.review.ReviewTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Review Tag Service 개발
@Service
@RequiredArgsConstructor
public class ReviewTagService {

    private final ReviewTagRepository reviewTagRepository;

    public List<ReviewTag> findReviewTagsByReviewId(Long reviewId) {
        return reviewTagRepository.findAllByReview_Id(reviewId);
    }
}
