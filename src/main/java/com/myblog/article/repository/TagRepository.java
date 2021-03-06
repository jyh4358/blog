package com.myblog.article.repository;

import com.myblog.article.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    @Query("select t from Tag t " +
            "join fetch t.articleTags")
    List<Tag> findByArticle_Id(Long articleId);
}
