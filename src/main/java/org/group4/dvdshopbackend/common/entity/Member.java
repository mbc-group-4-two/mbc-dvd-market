package org.group4.dvdshopbackend.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.group4.dvdshopbackend.common.enums.Role;
import org.group4.dvdshopbackend.core.BaseEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @SequenceGenerator(
            name = "member_seq",
            sequenceName = "member_seq_tbl",
            allocationSize = 1 // sequence 캐싱 처리, 배포시 50정도 -> 병목시 늘리기
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String deletedYn;

    @Column(name = "token_version", nullable = false)
    private Long tokenVersion = 0L;

    @PrePersist
    public void prePersist() {
        if (this.deletedYn == null) this.deletedYn = "N";

        if (this.role == null) this.role = Role.USER;

    }

    public int getTokenVersion() {
        return tokenVersion.intValue();
    }
}
