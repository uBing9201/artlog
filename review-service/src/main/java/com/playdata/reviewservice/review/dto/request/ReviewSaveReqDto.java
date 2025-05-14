package com.playdata.reviewservice.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveReqDto {

    private String contentId;

    // NULL 가능
    private String reviewContent;
    private String picUrl;
}
