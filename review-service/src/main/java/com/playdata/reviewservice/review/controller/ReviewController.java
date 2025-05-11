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

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody ReviewSaveReqDto dto) throws FeignServiceException, EntityNotFoundException {
        ReviewDefaultResDto resDto = reviewService.insert(dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "리뷰가 등록되었습니다.", resDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReviewUpdateReqDto dto) throws InvalidAccessReviewException {
        ReviewDefaultResDto resDto = reviewService.update(id, dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "리뷰가 수정되었습니다.", resDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws InvalidAccessReviewException {
        ReviewDefaultResDto resDto = reviewService.delete(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "리뷰가 삭제되었습니다.", resDto));
    }

    @GetMapping("/findByContentId/{contentId}")
    public ResponseEntity<?> findByContentId(@PathVariable Long contentId) throws EntityNotFoundException {
        List<ReviewResDto> resDtoList = reviewService.findByContentId(contentId);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "해당 Id에 대한 리뷰가 정상적으로 조회되었습니다.", resDtoList));
    }

}
