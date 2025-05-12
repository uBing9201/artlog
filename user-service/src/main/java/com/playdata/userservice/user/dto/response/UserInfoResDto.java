package com.playdata.userservice.user.dto.response;

import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.entity.UserCoupon;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResDto {

    private String userId;
    private int hintKey;
    private String hintValue;
    private String userName;
    private String email;
    private String phone;

    public UserInfoResDto(User user) {
        this.userId = user.getUserId();
        this.hintKey = user.getHintKey().getCode();
        this.hintValue = user.getHintValue();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

}
