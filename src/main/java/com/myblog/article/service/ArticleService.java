package com.myblog.article.service;

import com.google.gson.Gson;
import com.myblog.article.dto.*;
import com.myblog.article.model.Article;
import com.myblog.article.model.ArticleTag;
import com.myblog.article.model.Tag;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.article.repository.ArticleSearchRepository;
import com.myblog.article.repository.ArticleTagRepository;
import com.myblog.article.repository.TagRepository;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.common.checker.RightLoginChecker;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.myblog.common.exception.ExceptionMessage.NOT_FOUND_CATEGORY;
import static com.myblog.common.exception.ExceptionMessage.NOT_FOUNT_ARTICLE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private static final int PAGE_SIZE = 8;

    private final ArticleRepository articleRepository;
    private final ArticleSearchRepository articleSearchRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;
    private final Gson gson;


    /*
        - 메인 페이지 인기 게시물 조회, size = 8
     */
    public List<PopularArticleResponse> findPopularArticle() {
        List<Article> findPopularArticleList = articleRepository.findTop6ByOrderByHitDesc();

        return findPopularArticleList.stream()
                .map(PopularArticleResponse::of)
                .collect(Collectors.toList());
    }


    /*
        - 게시물 작성
     */
    @Transactional
    public Article writeArticle(ArticleWriteDto articleWriteDto, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);

        Category category = categoryRepository
                .findById(articleWriteDto.getCategory())
                .orElseThrow(NOT_FOUND_CATEGORY::getException);

        List<Tag> tagList = getTags(articleWriteDto.getTags());
        List<ArticleTag> collect = getArticleTags(tagList);

        Article article = Article.createArticle(articleWriteDto, customOauth2User.getMember(), category, collect);
        return articleRepository.save(article);

    }


    /*
        - 게시물 수정을 위한 기존 게시물 상세 조회
     */
    public ArticleModifyResponse findModifyArticle(Long articleId, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);

        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        List<String> tags = articleTagRepository.findByArticle_Id(articleId).stream()
                .map(ArticleTag::getTag)
                .map(Tag::getName)
                .collect(Collectors.toList());

        return ArticleModifyResponse.of(article, tags);
    }


    /*
        - 게시물 수정
     */
    @Transactional
    public Long modifyArticle(Long articleId, ArticleWriteDto articleWriteDto, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);

        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        Category category = categoryRepository
                .findById(articleWriteDto.getCategory()).orElseThrow(NOT_FOUND_CATEGORY::getException);
        List<ArticleTag> findArticleTagList = articleTagRepository.findByArticle_Id(article.getId());
        articleTagRepository.deleteAll(findArticleTagList);

        List<Tag> tags = getTags(articleWriteDto.getTags());
        List<ArticleTag> collect = getArticleTags(tags);

        article.modifyArticle(
                articleWriteDto.getTitle(),
                articleWriteDto.getContent(),
                articleWriteDto.getThumbnailUrl(),
                category);
        article.addArticleTags(collect);

        return article.getId();
    }


    /*
        - 게시물 상세 조회 및 조회 수 증가
     */
    @Transactional
    public ArticleDetailResponse findArticleDetail(Long articleId, boolean hitCheck) {
        Article article = articleSearchRepository.findByArticleIdWithTags(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        if (hitCheck) {
            article.addHit();
        }

        List<String> tags = article.getArticleTags().stream()
                .map(ArticleTag::getTag)
                .map(Tag::getName)
                .collect(Collectors.toList());

        ArticleDetailResponse detailResponse = ArticleDetailResponse.of(article, tags, article.getMember().getUsername());

        List<SimpleArticle> simpleArticles = getSimpleArticleByCategory(article.getCategory());
        detailResponse.setSimpleArticles(simpleArticles);

        return detailResponse;
    }


    /*
        - 카테고리별 최신 게시물 요청, size = 8
     */
    public Page<ArticleCardBoxResponse> findArticleByCategory(String categoryTitle, Pageable pageable) {
        Page<Article> findArticle = null;

        if (categoryTitle.equals("ALL")) {
            findArticle = articleRepository.findAll(pageable);
        } else {
            Category category = categoryRepository.findByTitle(categoryTitle).orElseThrow(NOT_FOUND_CATEGORY::getException);
            List<String> childCategoryTitles = category.getChild().stream()
                    .map(Category::getTitle)
                    .collect(Collectors.toList());

            findArticle = articleSearchRepository.findSearchArticleByCategory(categoryTitle, childCategoryTitles, pageable);

        }

        return findArticle.map(ArticleCardBoxResponse::of);
    }


    /*
        - 무한 스크롤를 위한 최신 게시물 요청, size = 8
     */
    public List<ArticleCardBoxResponse> findRecentArticle(int page) {

        return articleRepository.findByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
                .map(ArticleCardBoxResponse::of)
                .getContent();
    }


    /*
        - title, content 에 검색어가 포함된 게시물 조회, size = 8
     */
    public Page<ArticleCardBoxResponse> findSearchArticle(String keyword, Pageable pageable) {

        return articleSearchRepository.findSearchArticleByKeyword(keyword, pageable)
                .map(ArticleCardBoxResponse::of);
    }


    /*
        - tag와 관련된 게시물 조회, size = 8
     */
    public Page<ArticleCardBoxResponse> findArticleByTag(String tag, Pageable pageable) {

        return articleSearchRepository.findSearchArticleByTag(tag, pageable)
                .map(ArticleCardBoxResponse::of);
    }


    /*
        - 게시물 삭제
     */
    @Transactional
    public void deleteArticle(Long articleId, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);
        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        articleRepository.delete(article);
    }


    /*
        ======= 서비스 로직 ========
     */
    private List<SimpleArticle> getSimpleArticleByCategory(Category category) {
        List<Article> simpleArticleByCategory = articleRepository.findTop6ByCategoryOrderByCreatedDateDesc(category);

        return simpleArticleByCategory.stream()
                .map(SimpleArticle::of)
                .collect(Collectors.toList());
    }

    private List<ArticleTag> getArticleTags(List<Tag> tagList) {

        return tagList.stream()
                .map(ArticleTag::createArticleTag)
                .collect(Collectors.toList());
    }

    private List<Tag> getTags(String tags) {
        List<Map<String,String>> tagsDtoArrayList = gson.fromJson(tags, ArrayList.class);
        List<Tag> tagList = tagsDtoArrayList.stream()
                .map(tag -> findOrCreateTag(tag.get("value")))
                .collect(Collectors.toList());

        return tagList;
    }

    private Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.createTag(tagName)));
    }

}
