package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.YnType;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponInsertReqDto {

    private Long userKey;
    private String serialNumber;

    public UserCoupon toEntity(Long couponKey) {
        return UserCoupon.builder()
                .user(new User(userKey))
                .couponKey(couponKey)
                .active(YnType.YES)
                .build();
    }

}
