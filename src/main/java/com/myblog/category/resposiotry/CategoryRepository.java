package com.myblog.category.resposiotry;

import com.myblog.category.dto.CategoryQueryDto;
import com.myblog.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select new com.myblog.category.dto.CategoryQueryDto(c.id, count(c.id)) " +
            "from Article a join a.category c group by c.id")
    List<CategoryQueryDto> findCategoryCount();
}
