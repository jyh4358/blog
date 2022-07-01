package com.myblog.temparticle.repository;

import com.myblog.temparticle.model.TempArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempArticleRepository extends JpaRepository<TempArticle, Long> {
}
