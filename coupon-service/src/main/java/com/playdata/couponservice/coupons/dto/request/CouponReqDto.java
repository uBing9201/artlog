package com.playdata.couponservice.coupons.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponReqDto {
    private String serialNumber;
    private LocalDateTime expireDate = null;
    private Integer period = null;
    private Integer count = null;
    private String couponTitle;

}
