package com.somartreview.reviewmate.domain.live.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveFeedbackRepository extends JpaRepository<LiveFeedback, Long> {

    void deleteByReservation_Id(Long reservationId);

    Optional<LiveFeedback> findByReservation_Id(Long reservationId);
}
