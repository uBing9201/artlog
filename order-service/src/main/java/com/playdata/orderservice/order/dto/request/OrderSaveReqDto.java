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
    @Positive
    private Long userKey;

    @Positive
    private Long contentId;

    // NULL 가능
    private Long userCouponKey;

    @PositiveOrZero
    private Long totalPrice;
}

