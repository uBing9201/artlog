package com.playdata.couponservice.coupons.dto.response;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CouponValidateDto {
    private Long id;
    private boolean valid;
}
