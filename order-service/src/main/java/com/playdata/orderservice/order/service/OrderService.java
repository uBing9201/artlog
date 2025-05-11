package com.playdata.orderservice.order.service;

import com.playdata.orderservice.common.exception.InvalidAccessOrderException;
import com.playdata.orderservice.order.dto.request.ReviewIdentifyReqDto;
import com.playdata.orderservice.order.dto.response.OrderCancelResDto;
import com.playdata.orderservice.order.dto.request.OrderSaveReqDto;
import com.playdata.orderservice.order.dto.response.OrderSaveResDto;
import com.playdata.orderservice.order.dto.response.ReviewIdentifyResDto;
import com.playdata.orderservice.order.entity.Orders;
import com.playdata.orderservice.order.entity.YnType;
import com.playdata.orderservice.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderSaveResDto insert(OrderSaveReqDto dto) {
        // TODO: Feign 요청을 통해 order 무결성 검사
        // 1. 선택한 쿠폰이 유효한지 검사 후 사용한 쿠폰 상태 및 수량 변경
        // 2. 콘텐츠가 예매 가능한 상태인지..는 프론트에서 해주시겠지..?

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

    public ReviewIdentifyResDto isOrdered(ReviewIdentifyReqDto reqDto) {
        Optional<Orders> order = orderRepository.findByUserKeyAndContentId(reqDto.getUserKey(), reqDto.getContentId());
        boolean isOrdered = order.isPresent();

        // 취소된 주문인지 확인
        if(isOrdered && order.get().getActive() == YnType.N) {
            isOrdered = false;
        }

        log.error("isOrdered: " + isOrdered);

        return ReviewIdentifyResDto.builder()
                .isValid(isOrdered)
                .build();
    }
}
