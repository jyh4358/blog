package com.myblog.comment.model;

import com.myblog.article.model.Article;
import com.myblog.common.model.BasicEntity;
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
public class Comment extends BasicEntity {

    @Column(nullable = false, length = 300)
    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean secret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Comment> child = new ArrayList<>();


    @Builder
    public Comment(String content, boolean secret, Article article, Comment parent, Member member) {
        this.content = content;
        this.secret = secret;
        this.article = article;
        this.parent = parent;
        this.member = member;
    }


    public static Comment createComment(String content,
                                        boolean secret,
                                        Article article,
                                        Comment parent,
                                        Member member) {
        return Comment.builder()
                .content(content)
                .secret(secret)
                .article(article)
                .parent(parent)
                .member(member)
                .build();
    }
}
