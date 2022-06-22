package com.myblog.article.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public ArticleTag(Tag tag) {
        this.tag = tag;
    }

    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    public static ArticleTag createArticleTag(Tag tag) {
        return new ArticleTag(tag);
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
