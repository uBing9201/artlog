package com.playdata.couponservice.coupons.entity;

import com.playdata.couponservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Column(nullable = true)
    private LocalDateTime expireDate;

    @Column(nullable = true)
    private Integer period;

    @Column(nullable = false)
    @Builder.Default
    private Character active = 'Y';

   @Column(nullable = true)
    private Integer count;

   @Column(nullable = false)
    private String couponTitle;

   public void changeCouponActive() {
       this.active = this.getActive() == 'Y' ? 'N' : 'Y';
   }

   public void decreaseCount() {
       this.count = this.count - 1;
   }
}
