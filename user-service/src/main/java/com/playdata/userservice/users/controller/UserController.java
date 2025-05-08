package com.playdata.userservice.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user") // user 관련 요청은 /user로 시작한다고 가정.
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
//
//    @GetMapping("/insert")
//    public String insert(@AuthenticationPrincipal TokenUserInfo userInfo) {}

}
