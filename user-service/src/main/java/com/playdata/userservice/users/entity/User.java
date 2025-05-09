package com.playdata.userservice.users.entity;

import com.playdata.userservice.common.converter.HintKeyTypeConverter;
import com.playdata.userservice.common.converter.YnTypeConverter;
import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.common.entity.YnType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Convert(converter = HintKeyTypeConverter.class)
    @Column(name = "hint_key", nullable = false)
    private HintKeyType hintKey;

    @Column(name = "hint_value", nullable = false)
    private String hintValue;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Convert(converter = YnTypeConverter.class)
    @Column(nullable = false, length = 1)
    private YnType active;

    // CascadeType.PERSIST로 설정하면 새로운 엔터티 생성만 처리하고 기존 엔터티 업데이트는
    // 자동으로 처리되지 않습니다. -> MERGE (부모 엔터티 업데이트 시 연관 엔터티도 함께 업데이트)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserCoupon> userCoupons;
}
