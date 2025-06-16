package com.playdata.userservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdminResDto {
    private Long id;
    private String username;
    private String email;
    private String role;
}
