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
    @PostMapping("/order/isOrdered")
    ResponseEntity<ReviewIdentifyResDto> isOrdered(@RequestBody ReviewIdentifyReqDto reqDto);

}
