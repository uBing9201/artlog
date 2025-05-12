package com.playdata.reviewservice.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewIdentifyReqDto {
    @NotBlank(message = "사용자 Key는 반드시 존재해야 합니다.")
    private Long userKey;

    @NotBlank(message = "콘텐츠 ID는 반드시 존재해야 합니다.")
    private Long contentId;
}
