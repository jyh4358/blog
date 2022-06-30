package com.myblog.article.repository;

import com.myblog.article.model.Article;
import com.myblog.category.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByArticleTags(String tag);

    List<Article> findTop6ByCategoryOrderByCreatedDateDesc(Category category);


    List<Article> findTop6ByOrderByHitDesc();

    Slice<Article> findByOrderByIdDesc(Pageable pageable);
}
