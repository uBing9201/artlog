package com.playdata.reviewservice.review.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ReviewResDto {
    private Long id;
    private Long userKey;
    private String contentId;
    private String reviewContent;
    private String picUrl;
    private LocalDateTime updateDate;


}
