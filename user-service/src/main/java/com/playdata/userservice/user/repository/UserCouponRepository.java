package com.playdata.userservice.user.repository;

import com.playdata.userservice.common.entity.YnType;
import com.playdata.userservice.user.entity.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    /**
     * 유저쿠폰 조회 - 활성화 되어있는 조건
     * @param userId
     * @param active
     * @return
     */
    List<UserCoupon> findByUserIdAndActive(Long userId, YnType active);

}
