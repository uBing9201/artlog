package com.playdata.orderservice.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @DeleteMapping("/user/deleteUserCoupon/{id}")
    ResponseEntity<?> deleteUserCoupon(@PathVariable Long id);
}
