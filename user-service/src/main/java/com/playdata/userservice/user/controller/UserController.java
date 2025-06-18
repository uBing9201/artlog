package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.common.dto.LoginResultDto;
import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.user.dto.request.UserCheckUpdatePw;
import com.playdata.userservice.user.dto.request.UserCouponInsertReqDto;
import com.playdata.userservice.user.dto.response.UserCouponResDto;
import com.playdata.userservice.user.dto.request.UserFindEmailAndPwReqDto;
import com.playdata.userservice.user.dto.request.UserInsertReqDto;
import com.playdata.userservice.user.dto.request.UserLoginDto;
import com.playdata.userservice.user.dto.request.UserUpdatePw;
import com.playdata.userservice.user.dto.request.UserUpdateReqDto;
import com.playdata.userservice.user.dto.request.UserVerifyHintReqDto;
import com.playdata.userservice.user.dto.response.*;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.entity.UserCoupon;
import com.playdata.userservice.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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
        LoginResultDto result = userService.login(loginDto);
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, result.getMessage(), null));
        }
        User user = result.getUser();
        String token = jwtProvider.createToken(String.valueOf(user.getId()), user.getRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("token", token);
        loginInfo.put("id", user.getId());
        loginInfo.put("role", user.getRole().toString());

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "로그인 성공", loginInfo);
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

    /**
     * 계정찾기 - 이메일로 힌트 요청
     * @param email
     * @return
     */
    @PostMapping("/findByHintKey")
    public ResponseEntity<?> findByHintKey(@RequestParam String email) {
        User user = userService.findByHint(email);
        UserHintKeyResDto hintKeyResDto = new UserHintKeyResDto(user);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "계정찾기 요청 성공", hintKeyResDto);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 계정찾기 - 힌트 답 검증
     * @param reqDto
     * @return
     */
    @PostMapping("/verifyUserIdHint")
    public ResponseEntity<?> verifyUserIdHint(@RequestBody UserVerifyHintReqDto reqDto) {
        User user = userService.findByHintValue(reqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "계정찾기 성공", user.getUserId());
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 비밀번호찾기 - id, email로 힌트 요청
     * @param reqDto
     * @return
     */
    @PostMapping("/findByUserIdAndEmail")
    public ResponseEntity<?> findByUserIdAndEmail(@RequestBody UserFindEmailAndPwReqDto reqDto) {
        User user = userService.findByHint(reqDto);
        UserHintKeyResDto hintKeyResDto = new UserHintKeyResDto(user);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "비밀번호 찾기 요청 성공", hintKeyResDto);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 비밀번호찾기 - 힌트 답 검증
     * @param reqDto
     * @return
     */
    @PostMapping("/verifyPasswordHint")
    public ResponseEntity<?> verifyPasswordHint(@RequestBody UserVerifyHintReqDto reqDto) {
        User user = userService.findByHintValue(reqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "비밀번호 찾기 힌트 검증 완료", user.getId());
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 비밀번호찾기 - 비밀번호 변경 완료
     * @param id
     * @return
     */
    @PostMapping("/updatePw/{id}")
    public ResponseEntity<?> updatePw(@PathVariable Long id, @Valid @RequestBody UserUpdatePw updatePwDto) {
        User user = userService.updatePw(id, updatePwDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "비밀번호 변경 완료", user.getUserName());
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 마이페이지 - 비밀번호변경
     * @param checkUpdatePwDto
     * @return
     */
    @PostMapping("/updatePw")
    public ResponseEntity<?> userCheckUpdatePw(@Valid @RequestBody UserCheckUpdatePw checkUpdatePwDto) {
        User user = userService.checkUpdatePw(checkUpdatePwDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "비밀번호 변경 완료", user.getUserName());
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 마이페이지
     * @param id
     * @return
     */
    @GetMapping("/mypage/{id}")
    public ResponseEntity<?> mypage(@PathVariable Long id) {
        User user = userService.mypage(id);
        UserInfoResDto infoResDto = new UserInfoResDto(user);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "마이페이지 요청 성공", infoResDto);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 유저쿠폰조회
     * @param id
     * @return
     */
    @GetMapping("/findCouponById/{id}")
    public ResponseEntity<?> findCouponById(@PathVariable Long id) {
        List<UserCoupon> userCoupons = userService.findCouponsByUserId(id);

        List<UserCouponResDto> couponResDto = userCoupons.stream()
                .map(UserCouponResDto::new)
                .toList();

        return ResponseEntity.ok().body(couponResDto);
    }

    /**
     * 유저 쿠폰 등록
     * @param couponInsertReqDto
     * @return
     */
    @PostMapping("/couponInsert")
    public ResponseEntity<?> couponInsert(@RequestBody UserCouponInsertReqDto couponInsertReqDto) {
        UserCouponInsertResDto saved = userService.userCouponSave(couponInsertReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "쿠폰등록 완료", saved);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 유저 힌트키 조회
     * @return
     */
    @GetMapping("/hintKeys")
    public ResponseEntity<?> getHintKeyTypes() {
        List<HintKeyResDto> getHintKeys =  Arrays.stream(HintKeyType.values())
                .map(hint -> new HintKeyResDto(hint.getCode(), hint.getDesc()))
                .toList();
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "유저 힌트키 조회 완료", getHintKeys);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 아이디 중복 체크
     * @param userId
     * @return
     */
    @GetMapping("/checkId/{userId}")
    public ResponseEntity<CommonResDto> validCheckId(@PathVariable String userId) {
        boolean available = userService.validCheckId(userId);
        String message = !available ? "사용 가능한 아이디입니다." : "이미 존재하는 아이디입니다.";
        CommonResDto resDto = new CommonResDto(
                HttpStatus.OK,
                message,
                available
        );
        return ResponseEntity.ok(resDto);
    }

    /**
     * 사용자 -> 관리자 권한 변경
     * @param userId
     * @return
     */
    @PostMapping("/convertAdmin/{userId}")
    public ResponseEntity<?> convertAdmin(@PathVariable Long userId) {
        boolean isChanged = userService.convertAdmin(userId);
        if(isChanged) {
            return ResponseEntity.ok().body(new  CommonResDto(HttpStatus.OK, "일반 사용자에서 관리자로 권한을 변경하였습니다.", userId));
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 관리자 -> 사용자 권한 변경
     * @param userId
     * @return
     */
    @PostMapping("/convertUser/{userId}")
    public ResponseEntity<?> convertUser(@PathVariable Long userId) {
        boolean isChanged = userService.convertUser(userId);
        if(isChanged) {
            return ResponseEntity.ok().body(new  CommonResDto(HttpStatus.OK, "관리자에서 일반 사용자로 권한을 변경하였습니다.", userId));
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 유저 전체 조회
     * @return
     */
    @PostMapping("/findAllUsers")
    public ResponseEntity<?> findAllUsers() {
        List<UserAdminResDto> resDto = userService.findAllUsers();
        return ResponseEntity.ok().body(resDto);
    }

    @DeleteMapping("/deleteUserCoupon/{id}")
    public ResponseEntity<?> deleteUserCoupon(@PathVariable Long id) {
        userService.deleteUserCoupon(id);
        return ResponseEntity.ok().body(id);
    }

}