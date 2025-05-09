package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtProvider;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.request.UserInsertReqDto;
import com.playdata.userservice.user.dto.request.UserLoginDto;
import com.playdata.userservice.user.dto.request.UserUpdateReqDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.service.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, Object> redisTemplate;

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
                "회원가입 완료", saved.getUserId());

        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 유저 로그인
     * @param loginDto 로그인 DTO
     * @return id, 토큰값, role 정보
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {

        User user = userService.login(loginDto);
        String token = jwtProvider.createToken(String.valueOf(user.getId()), user.getRole().toString());
        String refreshToken = jwtProvider.createRefreshToken(
                String.valueOf(user.getId()), user.getRole().toString());

        redisTemplate.opsForValue().set("user:refresh:" + user.getUserId(), refreshToken, 2, TimeUnit.MINUTES);

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("token", token);
        loginInfo.put("id", user.getId());
        loginInfo.put("role", user.getRole().toString());

        CommonResDto resDto = new CommonResDto(HttpStatus.OK,
                "로그인 성공", loginInfo);
        return ResponseEntity.ok(resDto);
    }

    /**
     * 회원정보수정
     * @param id
     * @param updateDto
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserUpdateReqDto updateDto) {
        User user = userService.update(id, updateDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "회원정보 수정완료", user.getId());

        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 회원 탈퇴
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User user = userService.delete(id);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "회원탈퇴 완료", user.getId());

        return ResponseEntity.ok().body(resDto);
    }
}
