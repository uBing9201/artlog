package com.playdata.reviewservice.review.service;

import com.playdata.reviewservice.common.exception.InvalidAccessReviewException;
import com.playdata.reviewservice.review.dto.request.ReviewSaveReqDto;
import com.playdata.reviewservice.review.dto.request.ReviewUpdateReqDto;
import com.playdata.reviewservice.review.dto.response.ReviewResDto;
import com.playdata.reviewservice.review.entity.Review;
import com.playdata.reviewservice.review.entity.YnType;
import com.playdata.reviewservice.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewResDto insert(ReviewSaveReqDto dto) {
        // TODO: Feign 사용하여 order 정보 있는지 확인 후 등록 진행

        Review review = Review.builder()
                .reviewContent(dto.getReviewContent())
                .userKey(dto.getUserKey())
                .pirUrl(dto.getPicUrl())
                .contentId(dto.getContentId())
                .active(YnType.Y)
                .deleted(YnType.N)
                .build();

        reviewRepository.save(review);

        return ReviewResDto.builder()
                .id(review.getId())
                .build();
    }

    @Transactional
    public ReviewResDto update(Long id, ReviewUpdateReqDto dto) throws InvalidAccessReviewException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new InvalidAccessReviewException("Invalid Access Review")
        );

        if (dto.getReviewContent() != null) review.setReviewContent(dto.getReviewContent());
        if (dto.getPirUrl() != null) review.setPirUrl(dto.getPirUrl());

        return ReviewResDto.builder()
                .id(id)
                .build();
    }

    @Transactional
    public ReviewResDto delete(Long id) throws InvalidAccessReviewException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new InvalidAccessReviewException("Invalid Access Review")
        );

        review.deletedReview();
        return ReviewResDto.builder()
                .id(id)
                .build();
    }
}
