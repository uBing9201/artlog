package com.playdata.userservice.user.dto.response;

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

    private String userId;
    private String hintKey;

    public UserHintKeyResDto(User user) {
        this.userId = user.getUserId();
        this.hintKey = user.getHintKey().getDesc();
    }

}
