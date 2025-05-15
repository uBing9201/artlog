package com.playdata.reviewservice.review.dto.response;

import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyResDto {
    private boolean isValid = true;
}
