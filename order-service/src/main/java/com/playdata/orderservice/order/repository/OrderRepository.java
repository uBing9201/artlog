package com.playdata.orderservice.order.repository;

import com.playdata.orderservice.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.userKey = :userKey AND o.contentId = :contentId")
    Optional<Orders> findByUserKeyAndContentId(@Param("userKey") Long userKey, @Param("contentId") Long contentId);

    List<Orders> findByUserKey(String userKey);
}
