package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInsertReqDto {

    @NotEmpty(message = "아이디는 필수입니다!")
    private String userId;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/~`|\\\\]).{8,}$",
            message = "비밀번호는 대소문자 각각 1자 이상, 특수문자 1자 이상을 포함해야 합니다."
    )
    @NotEmpty(message = "비밀번호는 필수입니다!")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "힌트키는 필수입니다!")
    private HintKeyType hintKey;

    @NotEmpty(message = "힌트값은 필수입니다!")
    private String hintValue;

    @NotEmpty(message = "이름은 필수입니다!")
    private String userName;

    @NotEmpty(message = "이메일은 필수입니다!")
    @Email(message = "이메일 유형이 맞지않습니다!")
    private String email;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다! (예: 010-1234-5678)")
    @NotEmpty(message = "전화번호는 필수입니다!")
    private String phone;

    public User toEntity(PasswordEncoder encoder) {
        return User.builder()
                .userId(userId)
                .password(encoder.encode(password))
                .hintKey(hintKey)
                .hintValue(hintValue)
                .userName(userName)
                .email(email)
                .phone(phone)
                .build();
    }
}
