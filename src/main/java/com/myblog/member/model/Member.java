package com.myblog.member.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String picUrl;

    @Enumerated(EnumType.STRING)
    private Role role;



    @Builder
    public Member(String userId, String name, String email, String picUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
    }
}
