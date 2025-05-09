package com.playdata.orderservice.order.dto.request;

import lombok.*;

@Getter @Setter @ToString
@Builder @AllArgsConstructor
@NoArgsConstructor
public class OrderSaveReqDto {
    private Long userKey;
    private Long contentId;
    private Long userCouponKey;
    private Long totalPrice;
}

