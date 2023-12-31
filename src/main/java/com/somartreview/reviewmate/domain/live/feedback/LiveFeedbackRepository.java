package com.somartreview.reviewmate.domain.live.feedback;

import com.somartreview.reviewmate.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LiveFeedbackRepository extends JpaRepository<LiveFeedback, Long> {

    @Transactional
    @Modifying
    @Query("delete from LiveFeedback lf where lf.id = :id")
    void deleteById(Long id);


    @Transactional
    @Modifying
    @Query("delete from LiveFeedback lf where lf.reservation in :reservations")
    void deleteAllByReservationsInQuery(List<Reservation> reservations);
}
