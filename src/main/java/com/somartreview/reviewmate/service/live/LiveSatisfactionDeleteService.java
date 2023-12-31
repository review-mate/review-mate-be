package com.somartreview.reviewmate.service.live;

import com.somartreview.reviewmate.domain.live.satisfaction.LiveSatisfactionRepository;
import com.somartreview.reviewmate.domain.reservation.Reservation;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LiveSatisfactionDeleteService {

    private final LiveSatisfactionRepository liveSatisfactionRepository;


    @Transactional
    public void deleteById(Long id) {
        validateExistById(id);

        liveSatisfactionRepository.deleteById(id);
    }

    private void validateExistById(Long id) {
        if (!liveSatisfactionRepository.existsById(id)) {
            throw new DomainLogicException(ErrorCode.LIVE_SATISFACTION_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteAllByReservations(List<Reservation> reservations) {
        liveSatisfactionRepository.deleteAllByReservationsInQuery(reservations);
    }
}
