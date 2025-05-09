package com.playdata.userservice.users.controller;

import com.playdata.userservice.common.auth.JwtProvider;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.users.dto.request.UserInsertReqDto;
import com.playdata.userservice.users.dto.request.UserLoginDto;
import com.playdata.userservice.users.dto.request.UserReqDto;
import com.playdata.userservice.users.entity.User;
import com.playdata.userservice.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user") // user 관련 요청은 /user로 시작한다고 가정.
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    /**
     * 유저 생성
     * @param insertDto 유저 생성 dto
     * @return userId 와 성공 코드 전송
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody UserInsertReqDto insertDto) {

        User saved = userService.userInsert(insertDto);
        // ResponseEntity는 응답을 줄 때 다양한 정보를 한번에 포장해서 넘길 수 있습니다.
        // 요청에 따른 응답 상태 코드, 응답 헤더에 정보를 추가, 일관된 응답 처리를 제공합니다.

        CommonResDto resDto
                = new CommonResDto(HttpStatus.CREATED,
                "User Insert", saved.getUserId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto reqDto) {
        return null;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>("test ok", HttpStatus.OK);
    }

}
