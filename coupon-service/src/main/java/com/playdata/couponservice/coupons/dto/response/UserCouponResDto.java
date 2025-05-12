package com.playdata.couponservice.coupons.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class UserCouponResDto {
    private Long id;
    private Long userKey;
    private Long couponKey;
    private LocalDateTime registDate;
}
