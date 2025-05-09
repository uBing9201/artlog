package com.playdata.orderservice.order.service;

import com.playdata.orderservice.common.exception.InvalidAccessOrderException;
import com.playdata.orderservice.order.dto.response.OrderCancelResDto;
import com.playdata.orderservice.order.dto.request.OrderSaveReqDto;
import com.playdata.orderservice.order.dto.response.OrderSaveResDto;
import com.playdata.orderservice.order.entity.Orders;
import com.playdata.orderservice.order.entity.YnType;
import com.playdata.orderservice.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderSaveResDto insert(OrderSaveReqDto dto) {
        // TODO: User에 Feign 넣어서 coupon 상태 변경

        Orders order = Orders.builder()
                .userKey(dto.getUserKey())
                .contentId(dto.getContentId())
                .userCouponKey(dto.getUserCouponKey())
                .totalPrice(dto.getTotalPrice())
                .active(YnType.Y)
                .build();

        orderRepository.save(order);

        return OrderSaveResDto.builder()
                .userKey(order.getUserKey())
                .totalPrice(order.getTotalPrice())
                .contentId(order.getContentId())
                .id(order.getId())
                .build();
    }

    @Transactional
    public OrderCancelResDto cancel(Long id) throws InvalidAccessOrderException {
        Orders order = orderRepository.findById(id).orElseThrow(
                () -> new InvalidAccessOrderException("Invalid Order Access")
        );

        if(order.getActive() == YnType.N){
            throw new InvalidAccessOrderException("Order is already not active");
        }

        // TODO: 1. 사용한 쿠폰 복구해주어야 함 -> 회의를 통해 이야기 해야 함
        // TODO: 2. 쿠폰 사용했는데 수량이 존재한다면 다시 복구해주어야 함 -> 회의를 통해 이야기 해야 함

        order.changActive();

        return OrderCancelResDto.builder()
                .id(id)
                .build();
    }
}
