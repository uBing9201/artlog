package com.playdata.reviewservice.review.repository;

import com.playdata.reviewservice.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByContentId(String contentId);

    List<Review> findByUserKey(Long userKey);
}
