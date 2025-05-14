package com.playdata.apiservice.dto.common;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class OrderInfoResDto {
    private Long id;
    private Long userKey;
    private String contentId;
    private Long totalPrice;
    private boolean active;
    private LocalDateTime registDate;
}
