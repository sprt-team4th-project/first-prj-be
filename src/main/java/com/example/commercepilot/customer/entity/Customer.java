package com.example.commercepilot.customer.entity;

import com.example.commercepilot.config.BaseEntity;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Entity
@SoftDelete(columnName = "is_deleted")
@NoArgsConstructor
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(nullable = false)
    private String customerName;

    // 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 전화번호
    @Column(nullable = false)
    private String callNumber;

    // 비밀번호 (암호화 저장)
    @Column(nullable = false)
    private String password;

    // 계정 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;





    // ===== 고객 정보 수정 =====
    public void updateInfo(String name, String email, String phone) {
        this.customerName = name;
        this.email = email;
        this.callNumber = phone;

    }

    // ===== 고객 상태 변경 =====
    public void changeStatus(CustomerStatus status) {
        this.status = status;

    }
}

