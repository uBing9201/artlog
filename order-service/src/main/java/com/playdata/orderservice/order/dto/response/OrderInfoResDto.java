package com.playdata.orderservice.order.dto.response;

import com.playdata.orderservice.order.entity.YnType;
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
