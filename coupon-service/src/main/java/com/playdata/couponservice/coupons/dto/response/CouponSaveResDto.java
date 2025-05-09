package com.playdata.couponservice.coupons.dto.response;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CouponSaveResDto {
    private Long id;
    private String couponTitle;
}
