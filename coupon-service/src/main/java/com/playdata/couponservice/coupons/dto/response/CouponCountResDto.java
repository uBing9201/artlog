package com.playdata.couponservice.coupons.dto.response;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CouponCountResDto {
    private Long id;
    private String couponTitle;
    private int count;
}
