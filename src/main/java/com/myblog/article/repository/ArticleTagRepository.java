package com.myblog.article.repository;

import com.myblog.article.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    @Query("select at from ArticleTag at " +
            "join fetch at.tag t where at.article.id = :article_id")
    List<ArticleTag> findByArticle_Id(@Param("article_id")Long article_id);
}
