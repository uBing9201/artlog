package com.playdata.userservice.user.service;

import com.playdata.userservice.user.dto.request.UserInsertReqDto;
import com.playdata.userservice.user.dto.request.UserLoginDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * 로그인
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
     * 중복체크 로직
     * @param fieldName 필드 이름
     * @param result repository 값
     */
    private void validateDuplicate(String fieldName, Optional<User> result) {
        result.ifPresent(user -> {
            throw new IllegalArgumentException("이미 존재하는 " + fieldName + " 입니다!");
        });
    }
}
