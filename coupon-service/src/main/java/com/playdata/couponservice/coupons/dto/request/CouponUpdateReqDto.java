package com.playdata.couponservice.coupons.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponUpdateReqDto {
    private Long id;
    private Integer count;
    private LocalDateTime expireDate;
}
