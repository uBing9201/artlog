package com.playdata.reviewservice.review.dto.request;

import lombok.*;
import org.springframework.web.bind.annotation.PutMapping;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateReqDto {
    private String reviewContent = null;
    private String pirUrl = null;
}
