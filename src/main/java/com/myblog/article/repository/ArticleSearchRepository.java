package com.myblog.article.repository;

import com.myblog.article.model.Article;
import com.myblog.article.model.QArticle;
import com.myblog.article.model.QArticleTag;
import com.myblog.article.model.QTag;
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
import static com.myblog.article.model.QArticleTag.*;
import static com.myblog.article.model.QTag.tag;
import static com.myblog.category.model.QCategory.category;


@Repository
@RequiredArgsConstructor
public class ArticleSearchRepository {
    private final JPAQueryFactory queryFactory;

    /*
        카테고리 별 article 리스트 페이징처리하여 가져오기
        부모 카테고리로 aritcle 조회하거나 자식 카테고리로 article 조회 시 동적 쿼리를 이용한 페이징 처리
     */
    public Page<Article> findSearchArticleByCategory(String categoryTitle, List<String> childCategoryTitles, Pageable pageable) {

        List<Article> content = getArticleByCategory(categoryTitle, pageable, childCategoryTitles);
        Long count = getArticleCountByCategory(categoryTitle, childCategoryTitles);

        return new PageImpl<>(content, pageable, count);
    }

    private List<Article> getArticleByCategory(String categoryTitle, Pageable pageable, List<String> childCategoryTitles) {
        List<Article> findArticleByCategory = queryFactory
                .selectFrom(article)
                .join(article.category, category).fetchJoin()
                .where(
                        categoryTitleEq(categoryTitle).or(childCategoriesTitleIn(childCategoryTitles))
                )
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return findArticleByCategory;
    }

    private Long getArticleCountByCategory(String categoryTitle, List<String> childCategoryTitles) {
        Long count = queryFactory
                .select(article.count())
                .from(article)
                .where(
                        categoryTitleEq(categoryTitle).or(childCategoriesTitleIn(childCategoryTitles))
                )
                .fetchOne();
        return count;
    }





    // 동적 쿼리 키워드 검색 메서드
    public Page<Article> findSearchArticleBykeyword(String keyword, Pageable pageable) {
        List<Article> findArticleByKeyword = getArticleBykeyword(keyword, pageable);
        Long count = getArticleCountByKeyword(keyword);

        return new PageImpl<>(findArticleByKeyword, pageable, count);
    }

    private List<Article> getArticleBykeyword(String keyword, Pageable pageable) {
        List<Article> findArticleByKeyword = queryFactory
                .selectFrom(article)
                .where(
                        keywordContains(keyword)
                )
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return findArticleByKeyword;
    }

    private Long getArticleCountByKeyword(String keyword) {
        Long count = queryFactory
                .select(article.count())
                .from(article)
                .where(
                        keywordContains(keyword)
                )
                .fetchOne();
        return count;
    }




    public Page<Article> findSearchArticleByTag(String tagName, Pageable pageable) {
        List<Article> findArticleByTag = getArticleByTag(tagName, pageable);
        Long count = getArticleCountByTag(tagName, pageable);

        return new PageImpl<>(findArticleByTag, pageable, count);
    }
    private List<Article> getArticleByTag(String tagName, Pageable pageable) {
        return queryFactory
                .selectFrom(article)
                .join(article.articleTags, articleTag)
                .join(articleTag.tag, tag)
                .where(tag.name.eq(tagName))
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    private Long getArticleCountByTag(String tagName, Pageable pageable) {
        return queryFactory.select(article.count())
                .from(article)
                .join(article.articleTags, articleTag)
                .join(articleTag.tag, tag)
                .where(tag.name.eq(tagName))
                .fetchOne();
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

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? article.title.contains(keyword).or(article.content.contains(keyword)) : null;
    }

}
