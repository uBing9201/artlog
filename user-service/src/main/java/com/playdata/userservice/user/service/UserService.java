package com.playdata.userservice.user.service;

import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.common.entity.YnType;
import com.playdata.userservice.user.dto.request.UserFindEmailAndPwReqDto;
import com.playdata.userservice.user.dto.request.UserInsertReqDto;
import com.playdata.userservice.user.dto.request.UserLoginDto;
import com.playdata.userservice.user.dto.request.UserUpdatePasswordReqDto;
import com.playdata.userservice.user.dto.request.UserUpdatePw;
import com.playdata.userservice.user.dto.request.UserUpdateReqDto;
import com.playdata.userservice.user.dto.request.UserVerifyHintReqDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

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
    public User login(UserLoginDto loginDto) {

        User user = userRepository.findByUserId(loginDto.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("ID가 존재하지 않습니다.")
        );

        if (!encoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
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

        if (encoder.matches(updatePwDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
        }

        user.updatePw(encoder.encode(updatePwDto.getPassword()));
        return user;
    }

    public User mypage(Long id) {
        return userRepository.findByIdWithCouponsAndActive(id, YnType.YES).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다.")
        );
    }
}
