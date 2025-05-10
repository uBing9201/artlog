package com.playdata.userservice.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "아이디는 필수입니다!")
    private String userId;

    @NotEmpty(message = "힌트값은 필수입니다!")
    private String hintValue;

}
