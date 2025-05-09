package com.playdata.userservice.user.dto.request;

import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.common.entity.YnType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInsertReqDto {

    @NotEmpty(message = "아이디는 필수입니다!")
    private String userId;
    @NotEmpty(message = "비밀번호는 필수입니다!")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    private Byte hintKey;
    @NotEmpty(message = "힌트값은 필수입니다!")
    private String hintValue;
    @NotEmpty(message = "이름은 필수입니다!")
    private String userName;
    @NotEmpty(message = "이메일은 필수입니다!")
    private String email;
    @NotEmpty(message = "전화번호는 필수입니다!")
    private String phone;

    public User toEntity(PasswordEncoder encoder) {
        return User.builder()
                .userId(userId)
                .password(encoder.encode(password))
                .hintKey(HintKeyType.fromCode(hintKey))
                .hintValue(hintValue)
                .userName(userName)
                .email(email)
                .phone(phone)
                .build();
    }
}
