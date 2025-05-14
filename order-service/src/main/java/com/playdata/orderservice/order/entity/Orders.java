package com.playdata.orderservice.order.entity;

import com.playdata.orderservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder @ToString
public class Orders extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userKey;

    @Column(nullable = false)
    private String contentId;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private YnType active = YnType.Y;

    @Column(nullable = true)
    private Long userCouponKey;

    
    public void changActive() {
        this.active = this.getActive() == YnType.Y ? YnType.N : YnType.Y;
    }
}
