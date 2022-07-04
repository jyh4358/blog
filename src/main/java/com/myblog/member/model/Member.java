package com.myblog.member.model;

import com.myblog.article.model.Article;
import com.myblog.comment.model.Comment;
import com.myblog.common.model.BasicEntity;
import com.myblog.security.oauth2.model.OAuth2Provider;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BasicEntity {

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String picUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(String userId,
                  String username,
                  String email,
                  String picUrl,
                  OAuth2Provider provider,
                  Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.picUrl = picUrl;
        this.provider = provider;
        this.role = role;
    }

    public static Member createMember(String userId, String username, String email, String picUrl, OAuth2Provider provider) {
        return Member.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .picUrl(picUrl)
                .provider(provider)
                .role(Role.USER)
                .build();
    }


    public void setUsername(String username) {
        this.username = username;
    }
}
