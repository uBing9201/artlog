package com.playdata.orderservice.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.apache.logging.log4j.message.Message;

@Getter @Setter @ToString
@Builder @AllArgsConstructor
@NoArgsConstructor
public class OrderSaveReqDto {
    private Long userKey;
    private String contentId;

    // NULL 가능
    private Long userCouponKey;
    private Long totalPrice;
}

