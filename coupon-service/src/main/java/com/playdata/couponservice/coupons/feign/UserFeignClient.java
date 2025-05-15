package com.playdata.couponservice.coupons.feign;

import com.playdata.couponservice.coupons.dto.response.UserCouponResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    /**
     * 사용자 ID로 사용자가 보유한 쿠폰 조회
     * @return id, userKey, couponKey, registDate
     */
    @GetMapping("/user/findCouponById/{id}")
    ResponseEntity<List<UserCouponResDto>> findCouponById(@PathVariable Long id);
}
