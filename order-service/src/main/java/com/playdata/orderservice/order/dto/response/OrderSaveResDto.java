package com.playdata.orderservice.order.dto.response;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class OrderSaveResDto {
    private Long id;
    private Long userKey;
    private Long contentId;
    private Long totalPrice;
}
