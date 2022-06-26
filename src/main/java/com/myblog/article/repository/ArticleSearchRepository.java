package com.myblog.article.repository;

import com.myblog.article.model.Article;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.myblog.article.model.QArticle.article;
import static com.myblog.category.model.QCategory.category;


@Repository
@RequiredArgsConstructor
public class ArticleSearchRepository {
    private final JPAQueryFactory queryFactory;

    /*
        카테고리 별 article 리스트 페이징처리하여 가져오기
        부모 카테고리로 aritcle 조회하거나 자식 카테고리로 article 조회 시 동적 쿼리를 이용한 페이징 처리
     */
    public Page<Article> findSearchArticle(String categoryTitle, List<String> childCategoryTitles, Pageable pageable) {

        List<Article> content = getArticleList(categoryTitle, pageable, childCategoryTitles);
        Long count = getCount(categoryTitle, childCategoryTitles);

        return new PageImpl<>(content, pageable, count);
    }

    // 동적 쿼리를 이용한 부모 카테고리 article, 자식 카테고리 article 조회 메서드
    private List<Article> getArticleList(String categoryTitle, Pageable pageable, List<String> childCategoryTitles) {
        List<Article> content = queryFactory
                .selectFrom(article)
                .join(article.category, category).fetchJoin()
                .where(
                        categoryTitleEq(categoryTitle).or(childCategoriesTitleIn(childCategoryTitles))
                )
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    // 동적 쿼리를 이용한 count 조회 메서드
    private Long getCount(String categoryTitle, List<String> childCategoryTitles) {
        Long count = queryFactory
                .select(article.count())
                .from(article)
                .where(
                        categoryTitleEq(categoryTitle).or(childCategoriesTitleIn(childCategoryTitles))
                )
                .fetchOne();
        return count;
    }



    /*
        동적 쿼리 메서드
     */
    private BooleanExpression categoryTitleEq(String categoryTitle) {
        return StringUtils.hasText(categoryTitle) ? category.title.eq(categoryTitle) : null;
    }

    private BooleanExpression childCategoriesTitleIn(List<String> childCategoryTitles) {
        return childCategoryTitles.size() > 0 ? category.title.in(childCategoryTitles) : null;
    }
}
