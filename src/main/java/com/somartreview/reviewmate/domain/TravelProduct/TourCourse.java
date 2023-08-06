package com.somartreview.reviewmate.domain.TravelProduct;

import com.somartreview.reviewmate.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TourCourse extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "tour_course_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_product_id", nullable = false)
    private PackageTravelProduct packageTravelProduct;

    public TourCourse(String name, LocalDateTime startTime, LocalDateTime endTime, PackageTravelProduct packageTravelProduct) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;

        packageTravelProduct.addTourCourse(this);
        this.packageTravelProduct = packageTravelProduct;
    }
}
