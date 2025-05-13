package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.user.entity.UserCoupon;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponReqDto {
    private Long id;
    private Long userKey;
    private Long couponKey;
    private LocalDateTime registDate;

    public UserCouponReqDto(UserCoupon userCoupon) {
        this.id = userCoupon.getId();
        this.userKey = userCoupon.getId();
        this.couponKey = userCoupon.getCouponKey();
        this.registDate = userCoupon.getRegistDate();
    }
}