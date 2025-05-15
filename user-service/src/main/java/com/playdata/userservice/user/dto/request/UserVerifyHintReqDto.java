package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.HintKeyType;
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
public class UserVerifyHintReqDto {

    @NotEmpty(message = "이메일은 필수입니다!")
    private String email;

    @NotNull(message = "힌트키는 필수입니다!")
    private int hintKey;

    @NotEmpty(message = "힌트값은 필수입니다!")
    private String hintValue;

}
