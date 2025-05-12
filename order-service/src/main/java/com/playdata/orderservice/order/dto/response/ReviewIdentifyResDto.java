package com.playdata.orderservice.order.dto.response;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyResDto {
    private boolean isValid = true;
}
