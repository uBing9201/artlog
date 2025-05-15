package com.playdata.apiservice.dto.api;

import lombok.*;

import java.util.Objects;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor

public class ContentResDto {
    private String contentId;
    private String contentTitle;
    private String contentVenue;
    private String contentUrl;
    private String contentThumbnail;
    private Long contentCharge;
    private Long contentPeriod;
    private String startDate;
    private String endDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContentResDto that)) return false;
        return Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contentId);
    }
}
