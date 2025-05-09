package com.playdata.reviewservice.common.auth;

import lombok.*;

@Setter @Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private Long id;
    private Role role;

}
