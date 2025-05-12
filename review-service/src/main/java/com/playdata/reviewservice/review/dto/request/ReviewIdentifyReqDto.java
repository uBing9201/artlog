package com.playdata.reviewservice.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyReqDto {
    @Positive
    private Long userKey;
    @Positive
    private Long contentId;
}
