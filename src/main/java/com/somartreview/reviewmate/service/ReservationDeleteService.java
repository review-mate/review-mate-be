package com.somartreview.reviewmate.service;

import com.somartreview.reviewmate.domain.reservation.Reservation;
import com.somartreview.reviewmate.domain.reservation.ReservationRepository;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.exception.ErrorCode;
import com.somartreview.reviewmate.service.live.LiveFeedbackDeleteService;
import com.somartreview.reviewmate.service.live.LiveSatisfactionDeleteService;
import com.somartreview.reviewmate.service.review.ReviewDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationDeleteService {

    private final ReservationRepository reservationRepository;
    private final LiveSatisfactionDeleteService liveSatisfactionDeleteService;
    private final LiveFeedbackDeleteService liveFeedbackDeleteService;
    private final ReviewDeleteService reviewDeleteService;


    @Transactional
    public void delete(Long id) {
        validateExistId(id);

        reservationRepository.deleteById(id);
        List<Long> reservationIds = List.of(id);
        deleteAllRelatedEntitiesByReservationIds(reservationIds);
    }


    private void validateExistId(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new DomainLogicException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    // 문제의 원인
    @Transactional
    public void deleteAllByTravelProductId(Long travelProductId) {
        List<Reservation> reservations = reservationRepository.findAllByTravelProductId(travelProductId);
        List<Long> reservationIds = reservations.stream().map(Reservation::getId).toList();

        reservationRepository.deleteAllByTravelProductId(travelProductId);
        deleteAllRelatedEntitiesByReservationIds(reservationIds);
    }

    private void deleteAllRelatedEntitiesByReservationIds(List<Long> reservationIds) {
        reviewDeleteService.deleteAllByReservationIds(reservationIds);
        liveSatisfactionDeleteService.deleteAllByReservationIds(reservationIds);
        liveFeedbackDeleteService.deleteAllByReservationIds(reservationIds);
    }
}
