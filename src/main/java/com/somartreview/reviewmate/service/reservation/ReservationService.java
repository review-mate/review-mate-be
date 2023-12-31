package com.somartreview.reviewmate.service.reservation;

import com.somartreview.reviewmate.domain.customer.Customer;
import com.somartreview.reviewmate.domain.reservation.Reservation;
import com.somartreview.reviewmate.domain.reservation.ReservationRepository;
import com.somartreview.reviewmate.domain.product.SingleTravelProduct;
import com.somartreview.reviewmate.dto.reservation.SingleTravelReservationCreateRequest;
import com.somartreview.reviewmate.dto.reservation.SingleTravelProductReservationResponse;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.service.customers.CustomerService;
import com.somartreview.reviewmate.service.live.LiveFeedbackDeleteService;
import com.somartreview.reviewmate.service.live.LiveSatisfactionDeleteService;
import com.somartreview.reviewmate.service.products.SingleTravelProductService;
import lombok.RequiredArgsConstructor;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.somartreview.reviewmate.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerService customerService;
    private final SingleTravelProductService singleTravelProductService;
    private final LiveSatisfactionDeleteService liveSatisfactionDeleteService;
    private final LiveFeedbackDeleteService liveFeedbackDeleteService;
    private final SchedulerService schedulerService;

    @Transactional
    public Long createSingleTravelProductReservation(String partnerDomain,
                                                     SingleTravelReservationCreateRequest request,
                                                     MultipartFile thumbnail) {
        final Customer customer = customerService.retreiveCustomer(partnerDomain, request.getCustomerCreateRequest());
        final SingleTravelProduct travelProduct = singleTravelProductService.retreiveSingleTravelProduct(partnerDomain, request.getSingleTravelProductCreateRequest(), thumbnail);

        schedulerService.registerLiveSatisfactionRequest(travelProduct.getId(), request.getStartDateTime());

        return reservationRepository.save(request.toEntity(customer, travelProduct)).getId();
    }

    public Reservation findByPartnerDomainAndPartnerCustomId(String partnerDomain, String partnerCustomId) {
        return reservationRepository.findByPartnerDomainAndPartnerCustomId(partnerDomain, partnerCustomId)
                .orElseThrow(() -> new DomainLogicException(RESERVATION_NOT_FOUND));
    }

    public List<Reservation> findByProductIdAndStartDateTime(Long productId, LocalDateTime localDateTime) {
        return reservationRepository.findByProductIdAndStartDateTime(productId, localDateTime);
    }

    public SingleTravelProductReservationResponse getSingleTravelProductReservationResponseById(Long id) {
        return reservationRepository.findByIdFetchJoin(id)
                .map(SingleTravelProductReservationResponse::new)
                .orElseThrow(() -> new DomainLogicException(RESERVATION_NOT_FOUND));
    }

    public List<SingleTravelProductReservationResponse> getSingleTravelProductReservationResponseByCustomerOrSingleTravelProduct(Long customerId, Long singleTravelProductId) {
        if (singleTravelProductId == null) {
            return reservationRepository.findAllByCustomerIdFetchJoin(customerId)
                    .stream().map(SingleTravelProductReservationResponse::new).toList();

        } else if (customerId == null) {
            return reservationRepository.findAllByTravelProductIdFetchJoin(singleTravelProductId)
                    .stream().map(SingleTravelProductReservationResponse::new).toList();

        } else {
            return reservationRepository.findAllByTravelProductIdAndCustomerIdFetchJoin(singleTravelProductId, customerId)
                    .stream().map(SingleTravelProductReservationResponse::new).toList();
        }
    }
}
