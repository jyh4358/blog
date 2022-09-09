package com.myblog.article.repository;

import com.myblog.article.model.Article;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.myblog.article.model.QArticle.article;
import static com.myblog.article.model.QArticleTag.articleTag;
import static com.myblog.article.model.QTag.tag;
import static com.myblog.category.model.QCategory.category;
import static com.myblog.member.model.QMember.member;


@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleQueryRepository {
    private final JPAQueryFactory queryFactory;

    /*
        - 카테고리별 최신 게시물 조회, size = 8
        - 부모 카테고리로 조회, 자식 카테고리로 조회 시 동적으로 해당 조건에 따른 리스트 조회
     */
    public Page<Article> findSearchArticleByCategory(String categoryTitle, List<String> childCategoryTitles, Pageable pageable) {

        List<Article> articleByCategory = getArticleByCategory(categoryTitle, pageable, childCategoryTitles);
        Long count = getArticleCountByCategory(categoryTitle, childCategoryTitles);

        return new PageImpl<>(articleByCategory, pageable, count);
    }



    /*
        - 검색어(keyword)로 게시물 조회, size = 8
        - title, content 에 검색어가 포함된 게시물 조회
    */
    public Page<Article> findSearchArticleByKeyword(String keyword, Pageable pageable) {
        List<Article> articleByKeyword = getArticleByKeyword(keyword, pageable);
        Long count = getArticleCountByKeyword(keyword);

        return new PageImpl<>(articleByKeyword, pageable, count);
    }


    /*
        - tag 로 게시물 조회, size = 8
    */
    public Page<Article> findSearchArticleByTag(String tagName, Pageable pageable) {
        List<Article> articleByTag = getArticleByTag(tagName, pageable);
        Long count = getArticleCountByTag(tagName);

        return new PageImpl<>(articleByTag, pageable, count);
    }


    /*
        - 게시물 상세 데이터 조회
     */
    public Optional<Article> findByArticleIdWithTags(Long articleId) {
        Article articleWithTags = queryFactory
                .selectFrom(article)
                .join(article.articleTags, articleTag).fetchJoin()
                .join(articleTag.tag, tag).fetchJoin()
                .join(article.category, category).fetchJoin()
                .join(article.member, member).fetchJoin()
                .where(article.id.eq(articleId))
                .fetchOne();
        return Optional.ofNullable(articleWithTags);
    }

    /*
        - 게시물 수정을 하기위한 기존 게시물 조회
     */
    public Optional<Article> findByArticleIdWithArticleTags(Long articleId) {
        Article articleWithArticleTags = queryFactory
                .selectFrom(article)
                .join(article.articleTags, articleTag).fetchJoin()
                .join(articleTag.tag, tag)
                .where(article.id.eq(articleId))
                .fetchOne();
        return Optional.ofNullable(articleWithArticleTags);
    }


    private List<Article> getArticleByCategory(String categoryTitle, Pageable pageable, List<String> childCategoryTitles) {
        return queryFactory
                .selectFrom(article)
                .join(article.category, category).fetchJoin()
                .where(
                        categoryTitleEq(categoryTitle).or(childCategoriesTitleIn(childCategoryTitles))
                )
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long getArticleCountByCategory(String categoryTitle, List<String> childCategoryTitles) {
        return queryFactory
                .select(article.count())
                .from(article)
                .where(
                        categoryTitleEq(categoryTitle).or(childCategoriesTitleIn(childCategoryTitles))
                )
                .fetchOne();
    }

    private List<Article> getArticleByKeyword(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(article)
                .where(keywordContains(keyword))
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long getArticleCountByKeyword(String keyword) {
        return queryFactory
                .select(article.count())
                .from(article)
                .where(keywordContains(keyword))
                .fetchOne();
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

    private Long getArticleCountByTag(String tagName) {
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
        return StringUtils.hasText(categoryTitle) || categoryTitle.equals("ALL") ? category.title.eq(categoryTitle) : null;
    }

    private BooleanExpression childCategoriesTitleIn(List<String> childCategoryTitles) {
        return childCategoryTitles.size() > 0 ? category.title.in(childCategoryTitles) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? article.title.contains(keyword).or(article.content.contains(keyword)) : null;
    }


}
