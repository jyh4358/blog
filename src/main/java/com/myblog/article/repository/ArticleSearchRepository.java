package com.myblog.article.repository;

import com.myblog.article.model.Article;
import com.myblog.article.model.QArticle;
import com.myblog.member.model.QMember;
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
public class ArticleSearchRepository {
    private final JPAQueryFactory queryFactory;

    /*
        카테고리 별 article 리스트 페이징처리하여 가져오기
        부모 카테고리로 article 조회하거나 자식 카테고리로 article 조회 시 동적 쿼리를 이용한 페이징 처리
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


    /*
    - title, content 에 검색어가 포함된 게시물 조회, size = 8
    */
    public Page<Article> findSearchArticleByKeyword(String keyword, Pageable pageable) {
        List<Article> findArticleByKeyword = getArticleByKeyword(keyword, pageable);
        Long count = getArticleCountByKeyword(keyword);

        return new PageImpl<>(findArticleByKeyword, pageable, count);
    }

    private List<Article> getArticleByKeyword(String keyword, Pageable pageable) {
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

    /*
        - tag와 관련된 게시물 조회, size = 8
    */
    public Page<Article> findSearchArticleByTag(String tagName, Pageable pageable) {
        List<Article> findArticleByTag = getArticleByTag(tagName, pageable);
        Long count = getArticleCountByTag(tagName);

        return new PageImpl<>(findArticleByTag, pageable, count);
    }

    private List<Article> getArticleByTag(String tagName, Pageable pageable) {
        return queryFactory
                .selectFrom(article)
                .where(tag.name.eq(tagName))
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long getArticleCountByTag(String tagName) {
        return queryFactory.select(article.count())
                .from(article)
                .where(tag.name.eq(tagName))
                .fetchOne();
    }

    /*
        - 게시물 상세 데이터 요청
     */
    public Optional<Article> findByArticleIdWithTags(Long articleId) {
        Article findArticleWithTags = queryFactory
                .selectFrom(QArticle.article)
                .join(QArticle.article.articleTags, articleTag).fetchJoin()
                .join(articleTag.tag, tag).fetchJoin()
                .join(article.category, category).fetchJoin()
                .join(article.member, member)
                .where(QArticle.article.id.eq(articleId))
                .fetchOne();
        return Optional.ofNullable(findArticleWithTags);
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
