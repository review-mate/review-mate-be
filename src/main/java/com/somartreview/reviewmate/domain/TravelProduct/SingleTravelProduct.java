package com.somartreview.reviewmate.domain.TravelProduct;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SingleTravelProduct extends TravelProduct {

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    public SingleTravelProduct(String clientSideProductId, String thumbnailUrl, String name, Float rating, LocalDateTime startTime, LocalDateTime endTime, Category category) {
        super(clientSideProductId, thumbnailUrl, name, rating);
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }
}