package com.myblog.article.model;

import com.myblog.category.model.Category;
import com.myblog.comment.model.Comment;
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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // todo - 나중에 조회수 기능 까지 넣어보자

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private Long hit;

    @Column(nullable = false)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> parentComments = new ArrayList<>();

    @Builder
    public Article(String title,
                   String content,
                   String thumbnailUrl,
                   Member member,
                   Category category) {
        this.title = title;
        this.content = content;
        this.hit = 0L;
        this.thumbnailUrl = thumbnailUrl;
        this.member = member;
        this.category = category;
    }
}

