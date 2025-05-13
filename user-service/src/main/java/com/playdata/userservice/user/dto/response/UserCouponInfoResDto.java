package com.playdata.userservice.user.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserCouponInfoResDto {
    private Long id;
    private Long userKey;
    private LocalDateTime registDate;
    private Long userCouponKey;
    private String couponTitle;
    private LocalDateTime expiredDate;
}
