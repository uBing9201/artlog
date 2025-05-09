package com.playdata.userservice.user.dto.request;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private String userId;
    private String password;
}
