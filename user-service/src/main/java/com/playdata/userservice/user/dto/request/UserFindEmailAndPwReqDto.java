package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.HintKeyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class UserFindEmailAndPwReqDto {

    @NotNull(message = "아이디는 필수입니다!")
    private String userId;

    @NotEmpty(message = "이메일은 필수입니다!")
    @Email(message = "이메일 유형이 맞지않습니다!")
    private String email;
}
