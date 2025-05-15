package com.playdata.couponservice.coupons.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserCouponInfoResDto {
    private Long id;
    private Long userKey;
    private LocalDateTime registDate;
    private Long userCouponKey;
    private String couponTitle;
    private LocalDateTime expiredDate;
    private String serialNumber;
}
