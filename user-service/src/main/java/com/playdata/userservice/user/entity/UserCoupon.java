package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.common.entity.YnType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "UserCoupons")
public class UserCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "coupon_key")
    private Long couponKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    @Builder.Default
    private YnType active = YnType.YES;

}
