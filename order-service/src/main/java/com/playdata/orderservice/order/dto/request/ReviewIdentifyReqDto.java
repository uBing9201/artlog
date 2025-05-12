package com.playdata.orderservice.order.dto.request;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyReqDto {
    private Long userKey;
    private Long contentId;
}
