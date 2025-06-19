package com.playdata.apiservice.dto.common;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @ToString
@Builder @NoArgsConstructor @AllArgsConstructor

public class OrderInfoResDto {
    private Long id;
    private Long userKey;
    private String contentId;
    private Long totalPrice;
    private boolean active;
    private LocalDateTime registDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderInfoResDto that)) return false;
        return active == that.active && Objects.equals(id, that.id) && Objects.equals(userKey, that.userKey) && Objects.equals(contentId, that.contentId) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(registDate, that.registDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userKey, contentId, totalPrice, active, registDate);
    }
}
