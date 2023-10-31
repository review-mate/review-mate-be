package com.somartreview.reviewmate.domain.review;

import com.somartreview.reviewmate.domain.reservation.Reservation;
import com.somartreview.reviewmate.dto.review.ReviewTagStatisticsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    boolean existsByReservation_Id(Long reservationId);


    @Query("select count(r.id) from Review r join r.reservation.travelProduct.partnerCompany c " +
            "where r.reservation.travelProduct.partnerCompany.partnerDomain = :partnerDomain")
    Long countByPartnerDomain(String partnerDomain);


    @Query("select count(r.id) from Review r where r.reservation in :reservations")
    Long countByReservations(List<Reservation> reservation);


    @Query("select r from Review r where r.reservation in :reservations")
    List<Review> findAllByReservation(List<Reservation> reservations);


    @Query("SELECT new com.somartreview.reviewmate.dto.review.ReviewTagStatisticsDto(rt.reviewProperty, rt.polarity, COUNT(rt)) " +
            "FROM ReviewTag rt " +
            "JOIN Review r ON rt.review.id = r.id " +
            "WHERE rt.review.reservation.travelProduct.id = :travelProductId " +
            "GROUP BY rt.reviewProperty, rt.polarity")
    List<ReviewTagStatisticsDto> findReviewTagStatisticsByTravelProductId(Long travelProductId);
}
