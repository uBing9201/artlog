package com.playdata.reviewservice.review.repository;

import com.playdata.reviewservice.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
