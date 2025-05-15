package com.playdata.userservice.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckUpdatePw {

    private Long id;

    @NotEmpty(message = "비밀번호는 필수입니다!")
    private String password;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/~`|\\\\]).{8,}$",
            message = "비밀번호는 대소문자 각각 1자 이상, 특수문자 1자 이상을 포함해야 합니다."
    )
    @NotEmpty(message = "변경할 비밀번호는 필수입니다!")
    @Size(min = 8, message = "변경할 비밀번호는 최소 8자 이상이어야 합니다.")
    private String newPassword;
}
