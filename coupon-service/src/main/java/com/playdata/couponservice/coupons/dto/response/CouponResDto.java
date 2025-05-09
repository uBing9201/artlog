package com.playdata.couponservice.coupons.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResDto {
    private Long id;
    private String serialNumber;
    private LocalDateTime expireDate = null;
    private Integer period = null;
    private Integer count = null;
    private String couponTitle;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
}
