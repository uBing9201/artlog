package com.playdata.apiservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "review-service")
public interface ReviewFeignClient {
    @GetMapping("/review/findByApiFeign")
    ResponseEntity<Boolean> findByApiFeign(@RequestParam String contentId, @RequestParam Long userKey);
}
