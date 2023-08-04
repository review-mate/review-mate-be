package com.somartreview.reviewmate.domain.travelProduct;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class PackageTravelProduct extends TravelProduct {

    @Column(nullable = false)
    @OneToMany(mappedBy = "packageTravelProduct", cascade = CascadeType.ALL)
    private List<TourCourse> tourCourses = new ArrayList<>();
}