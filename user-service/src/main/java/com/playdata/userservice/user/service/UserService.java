package com.playdata.userservice.user.service;

import com.playdata.userservice.common.dto.LoginResultDto;
import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.common.entity.YnType;
import com.playdata.userservice.user.dto.request.UserCheckUpdatePw;
import com.playdata.userservice.user.dto.request.UserCouponInsertReqDto;
import com.playdata.userservice.user.dto.request.UserFindEmailAndPwReqDto;
import com.playdata.userservice.user.dto.request.UserInsertReqDto;
import com.playdata.userservice.user.dto.request.UserLoginDto;
import com.playdata.userservice.user.dto.request.UserUpdatePw;
import com.playdata.userservice.user.dto.request.UserUpdateReqDto;
import com.playdata.userservice.user.dto.request.UserVerifyHintReqDto;
import com.playdata.userservice.user.dto.response.UserAdminResDto;
import com.playdata.userservice.user.dto.response.UserCouponInsertResDto;
import com.playdata.userservice.user.entity.Role;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.entity.UserCoupon;
import com.playdata.userservice.user.feign.CouponFeignClient;
import com.playdata.userservice.user.repository.UserCouponRepository;
import com.playdata.userservice.user.repository.UserRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final PasswordEncoder encoder;
    private final CouponFeignClient couponFeignClient;

    /**
     * 유저 생성
     * @param insertDto 유저 생성 dto
     * @return 저장된 user 객체 전달
     */
    public User userInsert(UserInsertReqDto insertDto) {
        validateDuplicate("ID", userRepository.findByUserId(insertDto.getUserId()));
        validateDuplicate("이메일", userRepository.findByEmail(insertDto.getEmail()));
        validateDuplicate("전화번호", userRepository.findByPhone(insertDto.getPhone()));

        return userRepository.save(insertDto.toEntity(encoder));
    }

    /**
     * 유저 로그인
     * @param loginDto 로그인 DTO
     * @return 회원 로그인
     */
    public LoginResultDto login(UserLoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByUserIdAndActive(loginDto.getUserId(), YnType.YES);

        if (optionalUser.isEmpty()) {
            return new LoginResultDto(false, "아이디 또는 비밀번호를 확인해주세요.", null);
        }

        User user = optionalUser.get();

        if (!encoder.matches(loginDto.getPassword(), user.getPassword())) {
            return new LoginResultDto(false, "아이디 또는 비밀번호를 확인해주세요.", null);
        }

        return new LoginResultDto(true, "로그인 성공", user);
    }

    /**
     * 회원정보 수정
     * @param id
     * @param updateDto
     */
    @Transactional
    public User update(Long id, UserUpdateReqDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        user.updateUser(updateDto.getHintKey(), updateDto.getHintValue(), updateDto.getEmail(), updateDto.getPhone());
        return user;
    }

    /**
     * 회원 탈퇴
     * @param id
     * @return
     */
    @Transactional
    public User delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        user.deleteUser();
        return user;
    }

    /**
     * 중복체크 로직
     * @param fieldName 필드 이름
     * @param result repository 값
     */
    private void validateDuplicate(String fieldName, Optional<User> result) {
        result.ifPresent(user -> {
            throw new IllegalArgumentException("이미 존재하는 " + fieldName + " 입니다!");
        });
    }

    /**
     * 계정찾기 - 이메일로 힌트 요청
     * @param email
     * @return
     */
    public User findByHint(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("가입된 이메일이 존재하지 않습니다.")
        );
    }

    /**
     * 계정찾기 - 힌트 답 검증
     * @param reqDto
     * @return
     */
    public User findByHintValue(UserVerifyHintReqDto reqDto) {
        User user = userRepository.findByEmailAndHintKey(reqDto.getEmail(),
                HintKeyType.fromCode(reqDto.getHintKey())).orElseThrow(
                () -> new EntityNotFoundException("찾으려고하는 계정이 존재하지 않습니다.")
        );
        if (!reqDto.getHintValue().equals(user.getHintValue())) {
            throw new IllegalArgumentException("힌트 답변 내용이 일치하지 않습니다.");
        }
        return user;
    }

    /**
     * 비밀번호찾기 - id, email로 힌트 요청
     * @param reqDto
     * @return
     */
    public User findByHint(UserFindEmailAndPwReqDto reqDto) {
        return userRepository.findByUserIdAndEmail(reqDto.getUserId(), reqDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("가입된 회원이 존재하지 않습니다.")
        );
    }

    /**
     * 비밀번호찾기 - 비밀번호 변경 완료
     * @param id
     * @return
     */
    @Transactional
    public User updatePw(Long id, UserUpdatePw updatePwDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다.")
        );

        if(encoder.matches(updatePwDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("변경하려는 비밀번호가 이전 비밀번호와 동일합니다.");
        }

        user.updatePw(encoder.encode(updatePwDto.getPassword()));
        return user;
    }

    @Transactional
    public User checkUpdatePw(UserCheckUpdatePw checkUpdatePwDto) {
        User user = userRepository.findById(checkUpdatePwDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다.")
        );

        if (!encoder.matches(checkUpdatePwDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        } else if(encoder.matches(checkUpdatePwDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("변경하려는 비밀번호가 이전 비밀번호와 동일합니다.");
        }

        user.updatePw(encoder.encode(checkUpdatePwDto.getNewPassword()));
        return user;
    }

    /**
     * 마이페이지
     * @param id
     * @return
     */
    public User mypage(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다.")
        );
    }

    /**
     * 유저쿠폰조회
     * @param userId
     * @return
     */
    public List<UserCoupon> findCouponsByUserId(Long userId) {
        return userCouponRepository.findByUserIdAndActive(userId, YnType.YES);
    }

    /**
     * 유저 쿠폰 등록
     *
     * @param couponInsertReqDto
     * @return
     */
    public UserCouponInsertResDto userCouponSave(UserCouponInsertReqDto couponInsertReqDto) {
        try {
            log.error(couponInsertReqDto.toString());
            Long couponKey = couponFeignClient.findBySerial(couponInsertReqDto.getSerialNumber()).getBody();
            log.error(couponKey.toString());
            UserCoupon userCoupon = couponInsertReqDto.toEntity(couponKey);
            UserCoupon saved = userCouponRepository.save(userCoupon);
            return UserCouponInsertResDto.fromEntity(saved);
        } catch (FeignException.NotFound e) {
            // 쿠폰이 없을 경우 404 응답을 반환
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다.");
        } catch (FeignException e) {
            // 서버 에러일 경우 500 응답을 반환
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생");
        }
    }

    /**
     * 아이디 중복 체크
     * @param userId
     * @return
     */
    public boolean validCheckId(String userId) {
        // 중복일 경우 예외를 던지고, 아니면 그대로 true 리턴
        return userRepository.existsByUserId(userId);
    }

    /**
     * 사용자의 권한을 ADMIN으로 변경
     * @param id
     * @return
     */
    public boolean convertAdmin(Long id) {
        try {
            // 사용자 찾기
            User user = userRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("존재하지 않는 사용자입니다.")
            );

            // 권한 업데이트
            user.updateRole(Role.ADMIN);
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<UserAdminResDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()) {
           throw new EntityNotFoundException("존재하지 않는 사용자입니다.");
        }


        return userList.stream()
                .map(user -> UserAdminResDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUserName())
                        .role(user.getRole().toString()).build()
                )
                .toList();
    }
}
