package com.playdata.orderservice.order.controller;

import com.playdata.orderservice.common.dto.CommonResDto;
import com.playdata.orderservice.common.exception.InvalidAccessOrderException;
import com.playdata.orderservice.order.dto.request.OrderSaveReqDto;
import com.playdata.orderservice.order.dto.request.ReviewIdentifyReqDto;
import com.playdata.orderservice.order.dto.response.OrderCancelResDto;
import com.playdata.orderservice.order.dto.response.OrderInfoResDto;
import com.playdata.orderservice.order.dto.response.OrderSaveResDto;
import com.playdata.orderservice.order.dto.response.ReviewIdentifyResDto;
import com.playdata.orderservice.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문 등록
     * @param dto userKey, contentId, userCouponKey, totalPrice
     * @return id, userKey, contentId, totalPrice
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody @Valid OrderSaveReqDto dto) {
        OrderSaveResDto resDto = orderService.insert(dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.CREATED, "주문이 완료되었습니다.", resDto));
    }

    /**
     * 주문 취소
     * @param id id
     * @return id
     * @throws InvalidAccessOrderException 해당 주문이 없거나 이미 취소됨
     */
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) throws InvalidAccessOrderException {
        OrderCancelResDto resDto = orderService.cancel(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "주문이 정상적으로 취소되었습니다.", resDto));
    }

    /**
     * 특정 유저와 콘텐츠에 대해 주문 상태 확인
     * Review-Service 에서 넘어옴
     * @param reqDto userKey, contentId
     * @return isValid
     */
    @PostMapping("/isOrdered")
    ResponseEntity<ReviewIdentifyResDto> isOrdered(@RequestBody @Valid ReviewIdentifyReqDto reqDto) {
        ReviewIdentifyResDto resDto = orderService.isOrdered(reqDto);
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 콘텐츠 예매 조회
     * @param userKey userKey
     * @return id, userKey, contentId, totalPrice, active, registDate
     * @throws EntityNotFoundException 해당 사용자의 주문 내역이 존재하지 않음
     */
    @GetMapping("/order/findByAll/{userKey}")
    ResponseEntity<?> findByAll(@PathVariable String userKey) throws EntityNotFoundException {
        List<OrderInfoResDto> resDtoList = orderService.findByAll(userKey);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "사용자의 모든 주문 정보가 조회되었습니다.", resDtoList));
    }

}
