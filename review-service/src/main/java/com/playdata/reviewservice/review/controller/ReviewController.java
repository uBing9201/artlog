package com.playdata.reviewservice.review.controller;

import com.playdata.reviewservice.common.dto.CommonResDto;
import com.playdata.reviewservice.common.exception.FeignServiceException;
import com.playdata.reviewservice.common.exception.InvalidAccessReviewException;
import com.playdata.reviewservice.review.dto.request.ReviewSaveReqDto;
import com.playdata.reviewservice.review.dto.request.ReviewUpdateReqDto;
import com.playdata.reviewservice.review.dto.response.ReviewDefaultResDto;
import com.playdata.reviewservice.review.dto.response.ReviewResDto;
import com.playdata.reviewservice.review.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * @param dto userKey, contentId, reviewContent, picUrl
     * @return id
     * @throws FeignServiceException feign 처리 중 오류
     * @throws EntityNotFoundException 유효한 주문 정보가 없음
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody @Valid ReviewSaveReqDto dto) throws FeignServiceException, EntityNotFoundException {
        ReviewDefaultResDto resDto = reviewService.insert(dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "리뷰가 등록되었습니다.", resDto));
    }

    /**
     * 리뷰 수정
     * @param id id
     * @param dto reviewContent, picUrl
     * @return id
     * @throws InvalidAccessReviewException 일치하는 리뷰 없음
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReviewUpdateReqDto dto) throws InvalidAccessReviewException {
        ReviewDefaultResDto resDto = reviewService.update(id, dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "리뷰가 수정되었습니다.", resDto));
    }

    /**
     * 리뷰 삭제
     * @param id id
     * @return id
     * @throws InvalidAccessReviewException 일치하는 리뷰 없음
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws InvalidAccessReviewException {
        ReviewDefaultResDto resDto = reviewService.delete(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "리뷰가 삭제되었습니다.", resDto));
    }

    /**
     * 컨텐츠 별 리뷰 조회
     * @param contentId contentId
     * @return userKey, contentId, reviewContent, picUrl, updateDate
     * @throws EntityNotFoundException 해당 컨텐츠에 리뷰가 존재하지 않음
     */
    @GetMapping("/findByContentId/{contentId}")
    public ResponseEntity<?> findByContentId(@PathVariable String contentId) throws EntityNotFoundException {
        List<ReviewResDto> resDtoList = reviewService.findByContentId(contentId);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "해당 Id에 대한 리뷰가 정상적으로 조회되었습니다.", resDtoList));
    }

    /**
     * 사용자 별 리뷰 조회
     * @param userKey userKey
     * @return userKey, contentId, reviewContent, picUrl, updateDate
     * @throws EntityNotFoundException 해당 사용자에 대한 리뷰가 존재하지 않음
     */
    @GetMapping("/findByUserKey/{userKey}")
    public ResponseEntity<?> findByUserKey(@PathVariable Long userKey) throws EntityNotFoundException {
        List<ReviewResDto> resDtoList = reviewService.findByUserKey(userKey);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "해당 사용자에 대한 리뷰가 정상적으로 조회되었습니다.", resDtoList));
    }
}
