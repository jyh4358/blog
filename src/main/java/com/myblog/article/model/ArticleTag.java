package com.myblog.article.model;

import com.myblog.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTag extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public ArticleTag(Tag tag) {
        this.tag = tag;
    }


    public static ArticleTag createArticleTag(Tag tag) {
        return new ArticleTag(tag);
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
