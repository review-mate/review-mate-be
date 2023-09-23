package com.somartreview.reviewmate.domain.live.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveFeedbackRepository extends JpaRepository<LiveFeedback, Long> {

    void deleteByReservation_Id(Long reservationId);
}