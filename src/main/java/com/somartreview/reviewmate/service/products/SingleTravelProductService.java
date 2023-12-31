package com.somartreview.reviewmate.service.products;

import com.somartreview.reviewmate.domain.partner.company.PartnerCompany;
import com.somartreview.reviewmate.domain.partner.seller.PartnerSeller;
import com.somartreview.reviewmate.domain.product.SingleTravelProduct;
import com.somartreview.reviewmate.domain.product.SingleTravelProductCategory;
import com.somartreview.reviewmate.domain.product.SingleTravelProductRepository;
import com.somartreview.reviewmate.dto.product.SingleTravelProductCreateRequest;
import com.somartreview.reviewmate.dto.product.SingleTravelProductUpdateRequest;
import com.somartreview.reviewmate.dto.product.SingleTravelProductConsoleElementResponse;
import com.somartreview.reviewmate.exception.DomainLogicException;
import com.somartreview.reviewmate.service.partners.company.PartnerCompanyService;
import com.somartreview.reviewmate.service.partners.seller.PartnerSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.somartreview.reviewmate.exception.ErrorCode.*;
import static com.somartreview.reviewmate.exception.ErrorCode.TRAVEL_PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SingleTravelProductService {

    private final SingleTravelProductRepository singleTravelProductRepository;
    private final TravelProductService travelProductService;
    private final PartnerCompanyService partnerCompanyService;
    private final PartnerSellerService partnerSellerService;


    @Transactional
    public SingleTravelProduct create(String partnerDomain, SingleTravelProductCreateRequest request, MultipartFile thumbnailFile) {
        validateUniquePartnerCustomId(partnerDomain, request.getPartnerCustomId());

        final PartnerCompany partnerCompany = partnerCompanyService.findByPartnerDomain(partnerDomain);
        request.setPartnerCompany(partnerCompany);
        final PartnerSeller partnerSeller = partnerSellerService.findById(request.getPartnerSellerId());
        request.setPartnerSeller(partnerSeller);

        String thumbnailUrl = uploadThumbnailOnS3(thumbnailFile);
        return singleTravelProductRepository.save(request.toEntity(thumbnailUrl));
    }

    private void validateUniquePartnerCustomId(String partnerDomain, String partnerCustomId) {
        if (singleTravelProductRepository.existsByPartnerCompany_PartnerDomainAndPartnerCustomId(partnerDomain, partnerCustomId)) {
            throw new DomainLogicException(TRAVEL_PRODUCT_NOT_UNIQUE_PARTNER_CUSTOM_ID);
        }
    }

    @Transactional
    public SingleTravelProduct retreiveSingleTravelProduct(String partnerDomain, SingleTravelProductCreateRequest singleTravelProductCreateRequest, MultipartFile thumbnailFile) {
        if (existsByPartnerDomainAndPartnerCustomIdAndPartnerSellerId(partnerDomain, singleTravelProductCreateRequest.getPartnerCustomId(), singleTravelProductCreateRequest.getPartnerSellerId())) {
            return findByPartnerDomainAndPartnerCustomId(partnerDomain, singleTravelProductCreateRequest.getPartnerCustomId());
        }

        return create(partnerDomain, singleTravelProductCreateRequest, thumbnailFile);
    }

    public boolean existsByPartnerDomainAndPartnerCustomIdAndPartnerSellerId(String partnerDomain, String partnerCustomId, Long partnerSellerId) {
        return singleTravelProductRepository.existsByPartnerCompany_PartnerDomainAndPartnerCustomIdAndPartnerSeller_Id(partnerDomain, partnerCustomId, partnerSellerId);
    }

    @Transactional
    public void update(String partnerDomain, String partnerCustomId, SingleTravelProductUpdateRequest request, MultipartFile thumbnailFile) {
        SingleTravelProduct foundTravelProduct = findByPartnerDomainAndPartnerCustomId(partnerDomain, partnerCustomId);
        String thumbnailUrl = uploadThumbnailOnS3(thumbnailFile);

        foundTravelProduct.update(request, thumbnailUrl);
    }

    private String uploadThumbnailOnS3(MultipartFile thumbnail) {
        //  Impl Uploading thumbnail to S3 and get the url
        if (thumbnail == null) {
            return null;
        }

        return "https://www.testThumbnailUrl.com";
    }

    public SingleTravelProduct findById(Long id) {
        return singleTravelProductRepository.findById(id)
                .orElseThrow(() -> new DomainLogicException(TRAVEL_PRODUCT_NOT_FOUND));
    }

    public SingleTravelProduct findByPartnerDomainAndPartnerCustomId(String partnerDomain, String partnerCustomId) {
        return singleTravelProductRepository.findByPartnerDomainAndPartnerCustomId(partnerDomain, partnerCustomId)
                .orElseThrow(() -> new DomainLogicException(TRAVEL_PRODUCT_NOT_FOUND));
    }

    public SingleTravelProductConsoleElementResponse getSingleTravelProductConsoleElementResponseById(Long id) {
        SingleTravelProduct singleTravelProduct = findById(id);

        return new SingleTravelProductConsoleElementResponse(singleTravelProduct);
    }

    public SingleTravelProductConsoleElementResponse getSingleTravelProductConsoleElementResponseByPartnerCustomId(String partnerDomain, String partnerCustomId) {
        SingleTravelProduct singleTravelProduct = singleTravelProductRepository.findByPartnerDomainAndPartnerCustomIdFetchJoin(partnerDomain, partnerCustomId)
                .orElseThrow(() -> new DomainLogicException(TRAVEL_PRODUCT_NOT_FOUND));

        return new SingleTravelProductConsoleElementResponse(singleTravelProduct);
    }

    public List<SingleTravelProductConsoleElementResponse> getSingleTravelProductConsoleElementResponsesByPartnerDomainAndTravelProductCategory(String partnerDomain, SingleTravelProductCategory singleTravelProductCategory) {
        return singleTravelProductRepository.findAllByPartnerDomainAndSingleTravelProductCategoryFetchJoin(partnerDomain, singleTravelProductCategory)
                .stream().map(SingleTravelProductConsoleElementResponse::new)
                .toList();
    }
}
