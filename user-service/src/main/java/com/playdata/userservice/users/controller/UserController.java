package com.playdata.userservice.users.controller;

import com.playdata.userservice.users.dto.UserLoginDto;
import com.playdata.userservice.users.dto.UserReqDto;
import com.playdata.userservice.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user") // user 관련 요청은 /user로 시작한다고 가정.
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody UserReqDto reqDto) {
        return null;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto reqDto) {
        return null;
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal String username) {
        return username;
    }

}
