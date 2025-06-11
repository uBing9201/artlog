package com.playdata.orderservice.order.service;

import com.playdata.orderservice.common.auth.TokenUserInfo;
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

import java.util.ArrayList;
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
    public OrderSaveResDto insert(TokenUserInfo userInfo, OrderSaveReqDto dto) {
        Orders order = Orders.builder()
                .userKey(userInfo.getId())
                .contentId(dto.getContentId())
                .userCouponKey(dto.getUserCouponKey())
                .totalPrice(dto.getTotalPrice())
                .active(YnType.Y)
                .build();

        orderRepository.save(order);

        return OrderSaveResDto.builder()
                .userKey(userInfo.getId())
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
        List<Orders> orderList = orderRepository.findByUserKeyAndContentId(reqDto.getUserKey(), reqDto.getContentId());
        boolean isOrdered = !orderList.isEmpty();

        boolean flag = false;
        if(isOrdered) {
            // 취소된 주문인지 확인
            for (Orders order : orderList) {
                if(order.getActive() == YnType.Y) {
                    flag = true;
                }
            }
        }
        if(!flag) isOrdered = false;

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
            return new ArrayList<>();
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
