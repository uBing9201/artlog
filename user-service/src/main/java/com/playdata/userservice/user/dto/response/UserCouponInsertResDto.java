package com.playdata.userservice.user.dto.response;

import com.playdata.userservice.common.entity.YnType;
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
public class UserCouponInsertResDto {

    private Long id;
    private Long userId;
    private Long couponKey;
    private YnType active;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;

    public static UserCouponInsertResDto fromEntity(UserCoupon userCoupon) {
        return new UserCouponInsertResDto(
                userCoupon.getId(),
                userCoupon.getUser().getId(),
                userCoupon.getCouponKey(),
                userCoupon.getActive(),
                userCoupon.getRegistDate(),
                userCoupon.getUpdateDate()
        );
    }

}