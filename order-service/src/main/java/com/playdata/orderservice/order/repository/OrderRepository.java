package com.playdata.orderservice.order.repository;

import com.playdata.orderservice.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
