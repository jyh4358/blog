package com.myblog.article.repository;

import com.myblog.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByArticleTags(String tag);
}
