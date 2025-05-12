package com.playdata.couponservice.coupons.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponReqDto {
    @NotBlank(message = "시리얼번호는 반드시 입력해야합니다.")
    private String serialNumber;

    // NULL 가능성이 있으므로 Validation 처리 하지 않음
    private LocalDateTime expireDate;
    private Integer period;
    private Integer count;

    @NotBlank(message = "쿠폰 이름은 반드시 입력해야합니다.")
    private String couponTitle;
}
