package com.playdata.couponservice.coupons.controller;

import com.playdata.couponservice.coupons.dto.CouponReqDto;
import com.playdata.couponservice.coupons.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody CouponReqDto dto) {

        return null;
    }

    @GetMapping("/isValid/{expireDate}")
    public ResponseEntity<?> isValid(@PathVariable String expireDate) {
        return null;
    }

    @GetMapping("/findByAll")
    public ResponseEntity<?> findByAll() {
        return null;
    }

    @PostMapping("/findByCount/{id}")
    public ResponseEntity<?> findByCount(@PathVariable Long id) {
        return null;
    }

}
