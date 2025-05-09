package com.playdata.userservice.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseEntity {

    @Column(name = "regist_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime registDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
