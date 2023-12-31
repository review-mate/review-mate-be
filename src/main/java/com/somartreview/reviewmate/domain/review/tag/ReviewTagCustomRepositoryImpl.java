package com.somartreview.reviewmate.domain.review.tag;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somartreview.reviewmate.dto.review.tag.ReviewTagClassificationDto;

import javax.persistence.EntityManager;
import java.util.*;

import static com.somartreview.reviewmate.domain.review.tag.QReviewTag.reviewTag;

public class ReviewTagCustomRepositoryImpl implements ReviewTagCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ReviewTagCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<ReviewTagClassificationDto> getDistinctReviewTagClassificationsByTravelProductId(Long travelProductId) {
        List<Tuple> groupByTags = queryFactory
                .select(reviewTag.reviewProperty, reviewTag.keyword)
                .from(reviewTag)
                .where(reviewTag.review.reservation.travelProduct.id.eq(travelProductId))
                .groupBy(reviewTag.reviewProperty, reviewTag.keyword)
                .orderBy(reviewTag.keyword.asc())
                .fetch();

        return groupByTags.stream().map(tag ->
                new ReviewTagClassificationDto(tag.get(reviewTag.reviewProperty), tag.get(reviewTag.keyword))
        ).toList();
    }
}
