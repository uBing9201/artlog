package com.playdata.userservice.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "coupon-service")
public interface    CouponFeignClient {

    /**
     * 사용자 쿠폰 조회
     * @param userKey userKey
     * @return id, userKey, userCoupon.registDate, userCouponKey, couponTitle, expiredDate
     */
    @GetMapping("/coupon/findByUserKey/{userKey}")
    ResponseEntity<?> findByUserKey(@PathVariable Long userKey);

    /**
     * 쿠폰 ID 조회
     * @param serialNumber
     * @return
     */
    @GetMapping("/coupon/findBySerial/{serialNumber}")
    ResponseEntity<Long> findBySerial(@PathVariable String serialNumber);

}
