package com.yukeon.wantedpreonboardingbackend.member.domain;

import com.yukeon.wantedpreonboardingbackend.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String email,
                  String password,
                  Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
