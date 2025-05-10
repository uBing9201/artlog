package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.user.entity.User;
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
public class UserUpdateReqDto {

    @NotNull(message = "힌트키는 필수입니다!")
    private HintKeyType hintKey;

    @NotEmpty(message = "힌트값은 필수입니다!")
    private String hintValue;

    @NotEmpty(message = "이메일은 필수입니다!")
    @Email(message = "이메일 유형이 맞지않습니다!")
    private String email;

    @NotEmpty(message = "전화번호는 필수입니다!")
    private String phone;
}
