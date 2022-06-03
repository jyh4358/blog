package com.myblog.comment.model;

import com.myblog.article.model.Article;
import com.myblog.member.model.Member;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Comment> child = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean secret;

    @Builder
    public Comment(Article article, Comment parent, Member member, String content, boolean secret) {
        this.article = article;
        this.parent = parent;
        this.member = member;
        this.content = content;
        this.secret = secret;
    }
}
