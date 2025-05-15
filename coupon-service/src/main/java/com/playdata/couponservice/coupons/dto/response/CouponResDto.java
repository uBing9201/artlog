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
    private LocalDateTime expireDate;
    private Integer period;
    private Integer count;
    private String couponTitle;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
}
