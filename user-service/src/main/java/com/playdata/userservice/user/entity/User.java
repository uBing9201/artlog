package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.converter.HintKeyTypeConverter;
import com.playdata.userservice.common.converter.YnTypeConverter;
import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.common.entity.HintKeyType;
import com.playdata.userservice.common.entity.YnType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Convert(converter = YnTypeConverter.class)
    @Builder.Default
    @Column(nullable = false, length = 1)
    private YnType active = YnType.YES;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "user_role")
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserCoupon> userCoupons;

    public User(Long id) {
        this.id = id;
    }

    /**
     * 회원정보 수정
     * @param hintKey
     * @param hintValue
     * @param email
     * @param phone
     */
    public void updateUser(HintKeyType hintKey, String hintValue, String email, String phone) {
        this.hintKey = hintKey;
        this.hintValue = hintValue;
        this.email = email;
        this.phone = phone;
    }

    /**
     * 회원 탈퇴
     */
    public void deleteUser() {
        this.active = YnType.NO;
    }

    /**
     * 비밀번호 변경
     * @param password
     */
    public void updatePw(String password) {
        this.password = password;
    }


    /**
     * 권한 변경
     * @param role
     */
    public void updateRole(Role role) {
        this.role = role;
    }

}
