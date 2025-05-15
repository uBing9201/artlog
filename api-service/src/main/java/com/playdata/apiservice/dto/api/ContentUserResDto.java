package com.playdata.apiservice.dto.api;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor

public class ContentUserResDto {
    // (order) id, userKey, contentId, totalPrice, (order) active, registDate, isReviewed
    private Long id;
    private Long userKey;
    private String contentId;
    private Long totalPrice;
    private Boolean active;
    private LocalDateTime registDate;
    private Boolean isReviewed;
    private String contentTitle;
    private String contentThumbnail;

    private String contentVenue;
    private String contentUrl;
    private Long contentCharge;
    private Long contentPeriod;
    private String startDate;
    private String endDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContentUserResDto that)) return false;
        return Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contentId);
    }
}
