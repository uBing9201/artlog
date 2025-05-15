package com.playdata.userservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // 테이블과 관련이 없고, 컬럼 정보만 자식에게 제공하기 위해서 사용하는 아노테이션.
// 직접 사용되지 않고 반드시 상속을 통해 구현되어야 한다는 것을 강조하기 위해 abstract를 붙입니다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "regist_date", updatable = false)
    private LocalDateTime registDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
