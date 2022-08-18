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
        - 메인 페이지 인기 게시물 6개 요청
     */
    public List<PopularArticleResponse> findPopularArticle() {
        List<Article> findPopularArticleList = articleRepository.findTop6ByOrderByHitDesc();

        return findPopularArticleList.stream()
                .map(popularArticle -> PopularArticleResponse.of(popularArticle))
                .collect(Collectors.toList());
    }


    @Transactional
    public Article writeArticle(ArticleWriteDto articleWriteDto, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);

        Category category = categoryRepository
                .findById(articleWriteDto.getCategory()).orElseThrow(NOT_FOUND_CATEGORY::getException);

        List<Tag> tagList = getTags(articleWriteDto.getTags());
        List<ArticleTag> collect = getArticleTags(tagList);


        Article article = Article.createArticle(articleWriteDto, customOauth2User.getMember(), category, collect);
        return articleRepository.save(article);

    }



    public ArticleModifyResponse findModifyArticle(Long articleId, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);
        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        List<ArticleTag> findArticleTag = articleTagRepository.findByArticle_Id(articleId);
        List<String> tags = findArticleTag.stream()
                .map(s -> s.getTag().getName())
                .collect(Collectors.toList());

        ArticleModifyResponse articleModifyResponse = ArticleModifyResponse.of(article, tags);

        return articleModifyResponse;
    }


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
        - 게시물 상세 데이터 요청 및 조회 수 count
     */
    @Transactional
    public ArticleDetailResponse findArticleDetail(Long articleId, boolean hitCheck) {
        // todo - fetch join으로 변경할
        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        if (hitCheck) {
            article.addHit();
        }

        List<String> tags = articleTagRepository.findByArticle_Id(articleId).stream()
                .map(articleTag -> articleTag.getTag().getName())
                .collect(Collectors.toList());

        ArticleDetailResponse detailResponse = ArticleDetailResponse
                .of(article, tags, article.getMember().getUsername());

        List<SimpleArticle> simpleArticles = getSimpleArticleByCategory(article.getCategory());
        detailResponse.setSimpleArticles(simpleArticles);

        return detailResponse;
    }


    /*
        - 카테고리별 최신 게시물 8개 요청
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

        return findArticle.map(article -> ArticleCardBoxResponse.of(article));
    }

    /*
        - 무한 스크롤를 위한 최신 게시물 8개 씩 요청
     */
    public List<ArticleCardBoxResponse> findRecentArticle(int page) {

        return articleRepository.findByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
                .map(recentArticle -> ArticleCardBoxResponse.of(recentArticle))
                .getContent();
    }

    public Page<ArticleCardBoxResponse> findSearchArticle(String keyword, Pageable pageable) {

        return articleSearchRepository.findSearchArticleBykeyword(keyword, pageable)
                .map(articleByKeyword -> ArticleCardBoxResponse.of(articleByKeyword));
    }

    public Page<ArticleCardBoxResponse> findArticleByTag(String tag, Pageable pageable) {

        return articleSearchRepository.findSearchArticleByTag(tag, pageable)
                .map(articleByTag -> ArticleCardBoxResponse.of(articleByTag));
    }

    @Transactional
    public void deleteArticle(Long articleId, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkLoginMember(customOauth2User);
        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        articleRepository.delete(article);
    }

    /*
        서비스 로직
     */
    private List<SimpleArticle> getSimpleArticleByCategory(Category category) {
        List<Article> simpleArticleByCategory = articleRepository.findTop6ByCategoryOrderByCreatedDateDesc(category);

        return simpleArticleByCategory.stream()
                .map(articleByCategory -> SimpleArticle.of(articleByCategory))
                .collect(Collectors.toList());
    }

    private List<ArticleTag> getArticleTags(List<Tag> tagList) {

        return tagList.stream()
                .map(articleTag -> ArticleTag.createArticleTag(articleTag))
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
