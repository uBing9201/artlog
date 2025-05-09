package com.playdata.userservice.users.repository;

import com.playdata.userservice.users.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일 조회
     * @param email 이메일 객체
     * @return
     */
    Optional<User> findByEmail(String email);
}
