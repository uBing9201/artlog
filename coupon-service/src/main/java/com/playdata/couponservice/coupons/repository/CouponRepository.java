package com.playdata.couponservice.coupons.repository;

import com.playdata.couponservice.coupons.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> getCouponById(Long id);

    List<Coupon> getCouponBySerialNumber(String serialNumber);
}
