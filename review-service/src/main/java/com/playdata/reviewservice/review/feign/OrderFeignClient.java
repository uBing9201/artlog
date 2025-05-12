package com.playdata.reviewservice.review.feign;

import com.playdata.reviewservice.review.dto.response.ReviewIdentifyResDto;
import com.playdata.reviewservice.review.dto.request.ReviewIdentifyReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface OrderFeignClient {
    /**
     * 특정 유저와 콘텐츠에 대해 주문 상태 확인
     * @param reqDto userKey, contentId
     * @return isValid
     */
    @PostMapping("/order/isOrdered")
    ResponseEntity<ReviewIdentifyResDto> isOrdered(@RequestBody ReviewIdentifyReqDto reqDto);

}
