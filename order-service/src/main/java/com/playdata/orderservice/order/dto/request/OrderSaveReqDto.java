package com.playdata.orderservice.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.apache.logging.log4j.message.Message;

@Getter @Setter @ToString
@Builder @AllArgsConstructor
@NoArgsConstructor
public class OrderSaveReqDto {
    @NotBlank(message = "사용자 Key는 반드시 입력해야합니다.")
    private Long userKey;

    @NotBlank(message = "콘텐츠 ID는 반드시 입력해야합니다.")
    private Long contentId;

    // NULL 가능
    private Long userCouponKey;

    @PositiveOrZero
    private Long totalPrice;
}

