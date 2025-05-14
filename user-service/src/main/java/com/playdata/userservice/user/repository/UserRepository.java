package com.playdata.userservice.user.repository;

import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.common.entity.YnType;
import com.playdata.userservice.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /**
     * 유저의 이메일 + 힌트키 받아서 일치하는지 비교
     * @param email
     * @param hintKey
     * @return
     */
    Optional<User> findByEmailAndHintKey(String email, HintKeyType hintKey);

    /**
     * 비밀번호 찾기 - id,email 이 일치하는 항목 검색
     * @param userId
     * @param email
     * @return
     */
    Optional<User> findByUserIdAndEmail(String userId, String email);

    /**
     * 아이디 중복체크
     * @param userId
     * @return
     */
    boolean existsByUserId(String userId);

    /**
     * 유저 id, active 로 활성화된 계정만 조회
     * @param userId
     * @param ynType
     * @return
     */
    Optional<User> findByUserIdAndActive(String userId, YnType ynType);
}
