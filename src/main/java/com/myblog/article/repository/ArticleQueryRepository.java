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

import java.util.ArrayList;
import java.util.Collections;
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

    private final static int PRE_DEFAULT_SIZE = 3;
    private final static int NEXT_DEFAULT_SIZE = 4;
    private final static int TOTAL_DEFAULT_SIZE = 7;

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
                .distinct()
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
                .join(articleTag.tag, tag).fetchJoin()
                .join(article.category, category).fetchJoin()
                .distinct()
                .where(article.id.eq(articleId))
                .fetchOne();
        return Optional.ofNullable(articleWithArticleTags);
    }

    /*
        - 게시물과 관련된 카테고리의 게시물 6개 조회
        - 조회한 게시물 기준으로 이전 게시물 2개, 이후 게시물 4개와 조회 게시물 포함하여 총 6개 게시물 반환
     */
    public List<Article> getSimpleArticleByCategory(Long articleId, Long categoryId) {

        // articleId 기준으로 이전 게시물 6개 조회
        List<Article> preResult = queryFactory
                .selectFrom(article)
                .join(article.category, category)
                .where(
                        article.id.loe(articleId),
                        article.category.id.eq(categoryId)
                )
                .limit(6)
                .orderBy(article.id.desc())
                .fetch();

        // articleId 기준으로 이후 게시물 조회,
        // preResult(이전 게시물)이 없을 경우 그 갯수만큼 이후 게시물을 추가 조회
        List<Article> nextResult = queryFactory
                .selectFrom(article)
                .join(article.category, category)
                .where(
                        article.id.goe(articleId),
                        article.category.id.eq(categoryId)
                )
                .limit(NEXT_DEFAULT_SIZE + (preResult.size() < PRE_DEFAULT_SIZE ? PRE_DEFAULT_SIZE - preResult.size() : 0))
                .orderBy(article.id.asc())
                .fetch();

        Collections.reverse(nextResult);

        nextResult.addAll(preResult.subList(1, preResult.size() <= PRE_DEFAULT_SIZE ? preResult.size() : TOTAL_DEFAULT_SIZE - nextResult.size()));
        return nextResult;
    }


    // =====================================================================


    private List<Article> getArticleByCategory(String categoryTitle, Pageable pageable, List<String> childCategoryTitles) {
        return queryFactory
                .selectFrom(article)
                .join(article.category, category).fetchJoin()
                .distinct()
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
                .join(article.articleTags, articleTag).fetchJoin()
                .join(articleTag.tag, tag).fetchJoin()
                .distinct()
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
