package com.playdata.reviewservice.review.entity;

import com.playdata.reviewservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userKey;

    @Column(nullable = false)
    private String contentId;

    @Column(nullable = true)
    @Setter
    private String reviewContent;

    @Column(nullable = true)
    @Setter
    private String pirUrl;

    @Column(nullable = false)
    private YnType active;

    @Column(nullable = false)
    private YnType deleted;

    public void deletedReview() {
        this.deleted = YnType.Y;
    }

}
