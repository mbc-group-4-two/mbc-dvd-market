package org.group4.dvdshopbackend.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.group4.dvdshopbackend.common.enums.Role;
import org.group4.dvdshopbackend.core.BaseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @PrePersist
    public void prePersist() {
        if (this.deletedYn == null) {
            this.deletedYn = "N";
        }
    }

}
