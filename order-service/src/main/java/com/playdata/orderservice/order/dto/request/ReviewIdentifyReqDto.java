package com.playdata.orderservice.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyReqDto {
    private Long userKey;
    private Long contentId;
}
