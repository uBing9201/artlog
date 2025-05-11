package com.playdata.reviewservice.review.dto.request;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyReqDto {
    private Long userKey;
    private Long contentId;
}
