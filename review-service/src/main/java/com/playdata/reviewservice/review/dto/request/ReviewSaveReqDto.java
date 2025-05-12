package com.playdata.reviewservice.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveReqDto {
    @Positive
    private Long userKey;

    @Positive
    private Long contentId;

    // NULL 가능
    private String reviewContent;
    private String picUrl;
}
