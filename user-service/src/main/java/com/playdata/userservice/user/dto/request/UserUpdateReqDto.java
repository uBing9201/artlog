package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.HintKeyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다! (예: 010-1234-5678)")
    @NotEmpty(message = "전화번호는 필수입니다!")
    private String phone;
}
