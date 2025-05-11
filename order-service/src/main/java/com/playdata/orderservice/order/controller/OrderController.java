package com.playdata.orderservice.order.controller;

import com.playdata.orderservice.common.dto.CommonResDto;
import com.playdata.orderservice.common.exception.InvalidAccessOrderException;
import com.playdata.orderservice.order.dto.request.OrderSaveReqDto;
import com.playdata.orderservice.order.dto.request.ReviewIdentifyReqDto;
import com.playdata.orderservice.order.dto.response.OrderCancelResDto;
import com.playdata.orderservice.order.dto.response.OrderSaveResDto;
import com.playdata.orderservice.order.dto.response.ReviewIdentifyResDto;
import com.playdata.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody OrderSaveReqDto dto) {
        OrderSaveResDto resDto = orderService.insert(dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.CREATED, "주문이 완료되었습니다.", resDto));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) throws InvalidAccessOrderException {
        OrderCancelResDto resDto = orderService.cancel(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "주문이 정상적으로 취소되었습니다.", resDto));
    }


    // Open Feign
    @PostMapping("/isOrdered")
    ResponseEntity<ReviewIdentifyResDto> isOrdered(@RequestBody ReviewIdentifyReqDto reqDto) {
        ReviewIdentifyResDto resDto = orderService.isOrdered(reqDto);
        return ResponseEntity.ok().body(resDto);
    }
}
