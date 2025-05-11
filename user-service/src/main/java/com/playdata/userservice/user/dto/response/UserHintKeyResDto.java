package com.playdata.userservice.user.dto.response;

import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.user.entity.User;
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
public class UserHintKeyResDto {

    private String email;
    private String hintKeyDesc;
    private int hintKeyCode;

    public UserHintKeyResDto(User user) {
        this.email = user.getEmail();
        this.hintKeyDesc = user.getHintKey().getDesc();
        this.hintKeyCode = user.getHintKey().getCode();
    }

}
