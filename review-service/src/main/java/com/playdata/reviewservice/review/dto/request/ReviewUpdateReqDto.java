package com.playdata.reviewservice.review.dto.request;

import lombok.*;
import org.springframework.web.bind.annotation.PutMapping;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateReqDto {
    // NULL 가능
    private String reviewContent;
    private String pirUrl;
}
