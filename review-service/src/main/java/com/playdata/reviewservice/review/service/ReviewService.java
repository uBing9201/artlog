package com.playdata.reviewservice.review.service;

import com.playdata.reviewservice.common.exception.FeignServiceException;
import com.playdata.reviewservice.common.exception.InvalidAccessReviewException;
import com.playdata.reviewservice.review.dto.response.ReviewIdentifyResDto;
import com.playdata.reviewservice.review.dto.request.ReviewIdentifyReqDto;
import com.playdata.reviewservice.review.dto.request.ReviewSaveReqDto;
import com.playdata.reviewservice.review.dto.request.ReviewUpdateReqDto;
import com.playdata.reviewservice.review.dto.response.ReviewDefaultResDto;
import com.playdata.reviewservice.review.dto.response.ReviewResDto;
import com.playdata.reviewservice.review.entity.Review;
import com.playdata.reviewservice.review.entity.YnType;
import com.playdata.reviewservice.review.feign.OrderFeignClient;
import com.playdata.reviewservice.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderFeignClient orderFeignClient;

    public ReviewDefaultResDto insert(ReviewSaveReqDto dto) throws FeignServiceException, EntityNotFoundException {
        // Feign 사용하여 order 정보 있는지 확인 후 등록 진행
        ReviewIdentifyResDto orderDto = orderFeignClient.isOrdered(
                ReviewIdentifyReqDto.builder()
                        .contentId(dto.getContentId())
                        .userKey(dto.getUserKey())
                        .build()
        ).getBody();

        if (orderDto == null) {
            throw new FeignServiceException("Feign 과정에서 오류가 발생하였습니다.");
        }


        log.error("isOrdered: {}", orderDto.isValid());

        // 만약 유효하지 않다면
        if (orderDto.isValid()) {
            log.error("orderDto: {}", orderDto);

            Review review = Review.builder()
                    .reviewContent(dto.getReviewContent())
                    .userKey(dto.getUserKey())
                    .pirUrl(dto.getPicUrl())
                    .contentId(dto.getContentId())
                    .active(YnType.Y)
                    .deleted(YnType.N)
                    .build();

            reviewRepository.save(review);

            return ReviewDefaultResDto.builder()
                    .id(review.getId())
                    .build();
        } else {
            throw new EntityNotFoundException("해당 계정으로 구매된 내역이 존재하지 않습니다.");
        }


    }

    @Transactional
    public ReviewDefaultResDto update(Long id, ReviewUpdateReqDto dto) throws InvalidAccessReviewException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new InvalidAccessReviewException("Invalid Access Review")
        );

        if (dto.getReviewContent() != null) review.setReviewContent(dto.getReviewContent());
        if (dto.getPirUrl() != null) review.setPirUrl(dto.getPirUrl());

        return ReviewDefaultResDto.builder()
                .id(id)
                .build();
    }

    @Transactional
    public ReviewDefaultResDto delete(Long id) throws InvalidAccessReviewException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new InvalidAccessReviewException("Invalid Access Review")
        );

        review.deletedReview();
        return ReviewDefaultResDto.builder()
                .id(id)
                .build();
    }

    public List<ReviewResDto> findByContentId(Long contentId) throws EntityNotFoundException {
        List<Review> reviewList = reviewRepository.findByContentId(contentId);
        if(reviewList.isEmpty()) {
            throw new EntityNotFoundException("조회 결과가 없습니다.");
        }

        return reviewList.stream()
                .filter(review -> review.getActive() != YnType.N && review.getDeleted() != YnType.Y)
                .map(review -> ReviewResDto.builder()
                        .userKey(review.getUserKey())
                        .contentId(review.getContentId())
                        .updateDate(review.getUpdateDate())
                        .reviewContent(review.getReviewContent())
                        .picUrl(review.getPirUrl())
                        .build())
                .toList();
    }
}
