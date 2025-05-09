package com.playdata.reviewservice.review.dto.request;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveReqDto {
    private Long userKey;
    private Long contentId;
    private String reviewContent = null;
    private String picUrl = null;
}
