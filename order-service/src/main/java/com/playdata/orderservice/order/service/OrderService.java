package com.playdata.orderservice.order.service;

import com.playdata.orderservice.common.exception.InvalidAccessOrderException;
import com.playdata.orderservice.order.dto.request.ReviewIdentifyReqDto;
import com.playdata.orderservice.order.dto.response.OrderCancelResDto;
import com.playdata.orderservice.order.dto.request.OrderSaveReqDto;
import com.playdata.orderservice.order.dto.response.OrderInfoResDto;
import com.playdata.orderservice.order.dto.response.OrderSaveResDto;
import com.playdata.orderservice.order.dto.response.ReviewIdentifyResDto;
import com.playdata.orderservice.order.entity.Orders;
import com.playdata.orderservice.order.entity.YnType;
import com.playdata.orderservice.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    /**
     * 주문 등록
     * @param dto userKey, contentId, userCouponKey, totalPrice
     * @return id, userKey, contentId, totalPrice
     */
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

    /**
     * 주문 취소
     * @param id id
     * @return id
     * @throws InvalidAccessOrderException 해당 주문이 없거나 이미 취소됨
     */
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

    /**
     * 특정 유저와 콘텐츠에 대해 주문 상태 확인
     * Review-Service 에서 넘어옴
     * @param reqDto userKey, contentId
     * @return isValid
     */
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

    /**
     * 콘텐츠 예매 조회
     * @param userKey userKey
     * @return id, userKey, contentId, totalPrice, active, registDate
     * @throws EntityNotFoundException 해당 사용자의 주문 내역이 존재하지 않음
     */
    public List<OrderInfoResDto> findByAll(Long userKey) throws EntityNotFoundException {
        List<Orders> orderList = orderRepository.findByUserKey(userKey);
        if(orderList.isEmpty()) {
            throw new EntityNotFoundException("No orders found for userKey: " + userKey);
        }

        return orderList.stream()
                .map(order -> OrderInfoResDto.builder()
                        .id(order.getId())
                        .userKey(order.getUserKey())
                        .contentId(order.getContentId())
                        .registDate(order.getRegistDate())
                        .active(order.getActive() == YnType.Y)
                        .totalPrice(order.getTotalPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
