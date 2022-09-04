package com.myblog.article.model;

import com.myblog.article.dto.ArticleWriteRequest;
import com.myblog.category.model.Category;
import com.myblog.comment.model.Comment;
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
public class Article extends BasicEntity {

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private Long hit;


    @Column(columnDefinition = "varchar(255) default '/images/nothumbnail.jpg'", nullable = false)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public static Article createArticle(ArticleWriteRequest articleWriteRequest, Member member, Category category, List<ArticleTag> articleTags) {
        Article article = Article.builder()
                .title(articleWriteRequest.getTitle())
                .content(articleWriteRequest.getContent())
                .thumbnailUrl(articleWriteRequest.getThumbnailUrl())
                .member(member)
                .category(category)
                .build();
        article.addArticleTags(articleTags);
        if (article.getThumbnailUrl().isEmpty()) {
            article.defaultThumbnailUrl();
        }
        return article;
    }

    public void addArticleTags(List<ArticleTag> articleTagDto) {
        for (ArticleTag articleTag : articleTagDto) {
            articleTags.add(articleTag);
            articleTag.setArticle(this);
        }
    }

    public void modifyArticle(String title, String content, String thumbnailUrl, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
        if (thumbnailUrl.isEmpty()) {
            defaultThumbnailUrl();
        } else {
            this.thumbnailUrl = thumbnailUrl;
        }

    }

    public void addHit() {
        hit++;
    }

    public void defaultThumbnailUrl() {
        this.thumbnailUrl = "/images/nothumbnail.jpg";
    }
}

