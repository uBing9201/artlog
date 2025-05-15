package com.playdata.reviewservice.review.service;

import com.playdata.reviewservice.common.auth.TokenUserInfo;
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

    /**
     * 리뷰 등록
     *
     * @param userInfo
     * @param dto      userKey, contentId, reviewContent, picUrl
     * @return id
     * @throws FeignServiceException   feign 처리 중 오류
     * @throws EntityNotFoundException 유효한 주문 정보가 없음
     */
    public ReviewDefaultResDto insert(TokenUserInfo userInfo, ReviewSaveReqDto dto) throws FeignServiceException, EntityNotFoundException {
        log.error(dto.toString());
        // Feign 사용하여 order 정보 있는지 확인 후 등록 진행
        ReviewIdentifyResDto orderDto = orderFeignClient.isOrdered(
                ReviewIdentifyReqDto.builder()
                        .contentId(dto.getContentId())
                        .userKey(userInfo.getId())
                        .build()
        ).getBody();

        // 통신에 오류가 발생했다면
        if (orderDto == null) {
            throw new FeignServiceException("Feign 과정에서 오류가 발생하였습니다.");
        }

        // 만약 주문이 유효하지 않다면
        if (!orderDto.isValid()) {
            throw new EntityNotFoundException("해당 계정으로 구매된 내역이 존재하지 않습니다.");
        }

        // 리뷰 객체 생성 및 저장
        Review review = Review.builder()
                .reviewContent(dto.getReviewContent())
                .userKey(userInfo.getId())
                .pirUrl(dto.getPicUrl())
                .contentId(dto.getContentId())
                .active(YnType.Y)
                .deleted(YnType.N)
                .build();
        reviewRepository.save(review);

        return ReviewDefaultResDto.builder()
                .id(review.getId())
                .build();
    }

    /**
     * 리뷰 수정
     * @param id id
     * @param dto reviewContent, picUrl
     * @return id
     * @throws InvalidAccessReviewException 일치하는 리뷰 없음
     */
    @Transactional
    public ReviewDefaultResDto update(Long id, ReviewUpdateReqDto dto) throws InvalidAccessReviewException {
        // 해당하는 리뷰 찾기
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new InvalidAccessReviewException("Invalid Access Review")
        );

        // 리뷰가 삭제되었거나 활성화되어있지 않다면 예외 발생
        if(review.getActive() == YnType.N || review.getDeleted() == YnType.Y){
            throw new InvalidAccessReviewException("Invalid Access Review");
        }

        // 리뷰 수정 (어떤 유형의 리뷰라도 같은 로직을 사용할 수 있도록 구성)
        if (dto.getReviewContent() != null) review.setReviewContent(dto.getReviewContent());
        if (dto.getPirUrl() != null) review.setPirUrl(dto.getPirUrl());

        return ReviewDefaultResDto.builder()
                .id(id)
                .build();
    }

    /**
     * 리뷰 삭제
     * @param id id
     * @return id
     * @throws InvalidAccessReviewException 일치하는 리뷰 없음
     */
    @Transactional
    public ReviewDefaultResDto delete(Long id) throws InvalidAccessReviewException {
        // 삭제할 리뷰 조회
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new InvalidAccessReviewException("Invalid Access Review")
        );

        // 리뷰의 활성화 상태 변경
        review.deletedReview();

        return ReviewDefaultResDto.builder()
                .id(id)
                .build();
    }

    /**
     * 컨텐츠 별 리뷰 조회
     * @param contentId contentId
     * @return userKey, contentId, reviewContent, picUrl, updateDate
     * @throws EntityNotFoundException 해당 컨텐츠에 리뷰가 존재하지 않음
     */
    public List<ReviewResDto> findByContentId(String contentId) throws EntityNotFoundException {
        // 콘텐츠 ID 값으로 리뷰 조회
        List<Review> reviewList = reviewRepository.findByContentId(contentId);

        // 일치하는 값이 없다면 예외 발생
        if (reviewList.isEmpty()) {
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

    /**
     * 사용자 별 리뷰 조회
     * @param userKey userKey
     * @return userKey, contentId, reviewContent, picUrl, updateDate
     * @throws EntityNotFoundException 해당 사용자에 대한 리뷰가 존재하지 않음
     */
    public List<ReviewResDto> findByUserKey(Long userKey) {
        List<Review> reviewList = reviewRepository.findByUserKey(userKey);

        if(reviewList.isEmpty()) {
            throw new EntityNotFoundException("해당 유저가 작성한 리뷰가 존재하지 않음.");
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

    public boolean findByApiFeign(String contentId, Long userKey) {
        List<Review> resDto = reviewRepository.findByContentIdAndUserKey(contentId, userKey);
        if (resDto.isEmpty()) {
            return false;
        }

        return true;
    }
}
