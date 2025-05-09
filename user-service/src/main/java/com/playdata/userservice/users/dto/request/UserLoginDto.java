package com.playdata.userservice.users.dto.request;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private String user_id;
    private String password;
}
