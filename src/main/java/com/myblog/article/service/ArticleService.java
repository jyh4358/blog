package com.myblog.article.service;

import com.google.gson.Gson;
import com.myblog.article.dto.*;
import com.myblog.article.exception.NotExistArticleException;
import com.myblog.category.exception.NotExistCategoryException;
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
import com.myblog.security.oauth2.CustomOauth2User;
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
        System.out.println("findPopularArticleList.size() = " + findPopularArticleList.size());

        return findPopularArticleList.stream().map(s ->
                PopularArticleResponse.of(s)
        ).collect(Collectors.toList());
    }


    @Transactional
    public void writeArticle(ArticleWriteDto articleWriteDto, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkLoginMember(customOauth2User);

        Category category = categoryRepository.findById(articleWriteDto.getCategory()).get();

        List<Tag> tagList = getTags(articleWriteDto.getTags());
        List<ArticleTag> collect = getArticleTags(tagList);


        Article article = Article.createArticle(articleWriteDto, customOauth2User.getMember(), category, collect);
        articleRepository.save(article);

    }



    public ArticleModifyResponse findArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);
        List<ArticleTag> findArticleTag = articleTagRepository.findByArticle_Id(articleId);
        List<String> tags = findArticleTag.stream().map(s ->
                s.getTag().getName()).collect(Collectors.toList());

        ArticleModifyResponse articleModifyResponse = ArticleModifyResponse.of(article, tags);

        return articleModifyResponse;
    }


    @Transactional
    public Long modifyArticle(Long articleId, ArticleWriteDto articleWriteDto, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkLoginMember(customOauth2User);
        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);
        Category category = categoryRepository.findById(articleWriteDto.getCategory()).orElseThrow(NotExistCategoryException::new);
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
        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);
        if (hitCheck) {
            article.addHit();
        }
        List<ArticleTag> findArticleTag = articleTagRepository.findByArticle_Id(articleId);
        List<String> tags = findArticleTag.stream().map(s ->
                s.getTag().getName()).collect(Collectors.toList());

        ArticleDetailResponse detailResponse = ArticleDetailResponse.of(
                article, tags, article.getMember().getUsername());

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
            Category category = categoryRepository.findByTitle(categoryTitle).get();
            List<String> childCategoryTitles = category.getChild().stream().map(s -> s.getTitle()).collect(Collectors.toList());

            findArticle = articleSearchRepository.findSearchArticleByCategory(categoryTitle, childCategoryTitles, pageable);

        }

        Page<ArticleCardBoxResponse> articleCardBoxResponses = findArticle.map(s -> ArticleCardBoxResponse.of(s));
        return articleCardBoxResponses;
    }

    /*
        - 무한 스크롤를 위한 최신 게시물 8개 씩 요청
     */
    public List<PopularArticleResponse> findRecentArticle(int page) {
        Slice<Article> findRecentArticle = articleRepository.findByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE));
        Slice<PopularArticleResponse> popularArticleResponses = findRecentArticle.map(s ->
                PopularArticleResponse.of(s));
        return popularArticleResponses.getContent();

    }

    public Page<ArticleCardBoxResponse> findSearchArticle(String keyword, Pageable pageable) {
        Page<Article> findArticleByKeyword = articleSearchRepository.findSearchArticleBykeyword(keyword, pageable);
        Page<ArticleCardBoxResponse> articleByKeywordResponse = findArticleByKeyword.map(s -> ArticleCardBoxResponse.of(s));


        return articleByKeywordResponse;
    }

    public Page<ArticleCardBoxResponse> findArticleByTag(String tag, Pageable pageable) {
        Page<Article> findArticleByTag = articleSearchRepository.findSearchArticleByTag(tag, pageable);
        Page<ArticleCardBoxResponse> articleByTagResponse = findArticleByTag.map(s -> ArticleCardBoxResponse.of(s));

        return articleByTagResponse;
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);
        articleRepository.delete(article);
    }

    /*
        서비스 로직
     */
    private List<SimpleArticle> getSimpleArticleByCategory(Category category) {
        List<Article> simpleArticleByCategory = articleRepository.findTop6ByCategoryOrderByCreatedDateDesc(category);
        return simpleArticleByCategory.stream().map(
                s -> SimpleArticle.of(s)
        ).collect(Collectors.toList());
    }

    private List<ArticleTag> getArticleTags(List<Tag> tagList) {
        List<ArticleTag> collect = tagList.stream().map(
                s -> ArticleTag.createArticleTag(s)
        ).collect(Collectors.toList());
        return collect;
    }

    private Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(
                        () -> tagRepository.save(Tag.createTag(tagName))
                );
    }

    private List<Tag> getTags(String tags) {
        List<Map<String,String>> tagsDtoArrayList = gson.fromJson(tags, ArrayList.class);
        List<Tag> tagList = tagsDtoArrayList.stream().map(s ->
                findOrCreateTag(s.get("value"))
        ).collect(Collectors.toList());
        return tagList;
    }



}
