package com.playdata.apiservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "review-service")
public interface ReviewFeignClient {
    @GetMapping("/review/findByContentIdFeign/{contentId}")
    ResponseEntity<Boolean> findByContentFeignId(@PathVariable String contentId);
}
