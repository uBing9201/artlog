package com.playdata.userservice.user.repository;

import com.playdata.userservice.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일 조회
     * @param email 이메일 객체
     * @return
     */
    Optional<User> findByEmail(String email);

    /**
     * 유저 Id 조회
     * @param userId 유저 Id
     * @return
     */
    Optional<User> findByUserId(String userId);

    /**
     * 유저 Phone 조회
     * @param phone 유저 Id
     * @return
     */
    Optional<User> findByPhone(String phone);

}
