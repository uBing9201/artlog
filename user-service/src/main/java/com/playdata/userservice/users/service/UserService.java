package com.playdata.userservice.users.service;

import com.playdata.userservice.common.auth.JwtProvider;
import com.playdata.userservice.users.dto.request.UserInsertReqDto;
import com.playdata.userservice.users.entity.User;
import com.playdata.userservice.users.repository.UserRepository;
import jakarta.validation.Valid;
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
        
        // 이메일 중복 조회
        Optional<User> foundEmail
                = userRepository.findByEmail(insertDto.getEmail());

        if (foundEmail.isPresent()) {
            // 이메일 존재? -> 이메일 중복 -> 회원가입 불가!
            // 예외를 일부러 생성시켜서 컨트롤러가 감지하게 할겁니다.
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다!");
        }

        // 이메일 중복 안됨 -> 회원가입 진행하자.
        // dto를 entity 로 변환하는 로직이 필요!
        User user = insertDto.toEntity(encoder);
        return userRepository.save(user);
    }
}
