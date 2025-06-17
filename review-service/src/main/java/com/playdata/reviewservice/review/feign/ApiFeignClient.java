package com.playdata.reviewservice.review.feign;

import com.playdata.reviewservice.common.dto.ContentUserResDto;
import com.playdata.reviewservice.review.dto.request.ReviewIdentifyReqDto;
import com.playdata.reviewservice.review.dto.response.ReviewIdentifyResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "api-service")
public interface ApiFeignClient {
    @GetMapping("/feignUserData/{userKey}")
    ResponseEntity<List<ContentUserResDto>> feignUserData(@PathVariable Long userKey);

}
