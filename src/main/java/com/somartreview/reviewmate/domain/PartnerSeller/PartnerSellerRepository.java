package com.somartreview.reviewmate.domain.PartnerSeller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerSellerRepository extends JpaRepository<PartnerSeller, Long> {
}