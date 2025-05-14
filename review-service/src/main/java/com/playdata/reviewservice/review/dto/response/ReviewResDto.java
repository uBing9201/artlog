package com.playdata.reviewservice.review.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewResDto {
    private Long userKey;
    private String contentId;
    private String reviewContent = null;
    private String picUrl = null;
    private LocalDateTime updateDate;
}
