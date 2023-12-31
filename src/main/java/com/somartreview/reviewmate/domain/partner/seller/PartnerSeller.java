package com.somartreview.reviewmate.domain.partner.seller;

import com.somartreview.reviewmate.domain.BaseEntity;
import com.somartreview.reviewmate.domain.partner.company.PartnerCompany;
import com.somartreview.reviewmate.dto.partner.seller.PartnerSellerUpdateRequest;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.exception.ErrorCode;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor
public class PartnerSeller extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 255;
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{11}$");


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_seller_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String kakaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_company_id", nullable = false)
    private PartnerCompany partnerCompany;

    @Builder
    public PartnerSeller(String name, String phoneNumber, String kakaoId, PartnerCompany partnerCompany) {
        validateName(name);
        this.name = name;
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
        this.kakaoId = kakaoId;
        this.partnerCompany = partnerCompany;
    }

    private void validateName(final String name) {
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new DomainLogicException(ErrorCode.PARTNER_SELLER_NAME_ERROR);
        }
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if (phoneNumber.isBlank() || !PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new DomainLogicException(ErrorCode.CUSTOMER_PHONE_NUMBER_ERROR);
        }
    }

    public void update(PartnerSellerUpdateRequest request) {
        validateName(request.getName());
        this.name = request.getName();
        validatePhoneNumber(request.getPhoneNumber());
        this.phoneNumber = request.getPhoneNumber();
        this.kakaoId = request.getKakaoId();
    }
}