package com.playdata.userservice.users.service;

import com.playdata.userservice.common.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final JwtProvider jwtProvider;

}
