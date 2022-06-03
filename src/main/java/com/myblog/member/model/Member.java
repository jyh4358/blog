package com.myblog.member.model;

import com.myblog.article.model.Article;
import com.myblog.comment.model.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String picUrl;

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
                  Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.picUrl = picUrl;
        this.role = role;
    }
}
