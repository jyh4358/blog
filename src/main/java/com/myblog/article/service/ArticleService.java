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


        List<Map<String,String>> tagsDtoArrayList = gson.fromJson(articleWriteDto.getTags(), ArrayList.class);
        List<Tag> tags = tagsDtoArrayList.stream().map(s ->
                findOrCreateTag(s.get("value"))
        ).collect(Collectors.toList());

        List<ArticleTag> collect = tags.stream().map(
                tag -> ArticleTag.createArticleTag(tag)
                ).collect(Collectors.toList());


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
    public Long modifyArticle(Long articleId, ArticleWriteDto articleWriteDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);
        Category category = categoryRepository.findById(articleWriteDto.getCategory()).orElseThrow(NotExistCategoryException::new);
        List<ArticleTag> byArticle_id = articleTagRepository.findByArticle_Id(article.getId());
        articleTagRepository.deleteAll(byArticle_id);
        List<Map<String,String>> tagsDtoArrayList = gson.fromJson(articleWriteDto.getTags(), ArrayList.class);

        List<Tag> tags = tagsDtoArrayList.stream().map(s ->
                findOrCreateTag(s.get("value"))
        ).collect(Collectors.toList());

        List<ArticleTag> collect = tags.stream().map(
                tag -> ArticleTag.createArticleTag(tag)
        ).collect(Collectors.toList());

        article.modifyArticle(
                articleWriteDto.getTitle(),
                articleWriteDto.getContent(),
                articleWriteDto.getThumbnailUrl(),
                category);
        article.addArticleTags(collect);

        return article.getId();
    }

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

        List<SimpleArticle> simpleArticles = findArticleListByCategory(article.getCategory());
        detailResponse.setSimpleArticles(simpleArticles);

        return detailResponse;
    }


    public Page<ArticleCardBoxResponse> findSearchArticle(String categoryTitle, Pageable pageable) {
        Page<Article> findArticle = null;

        if (categoryTitle.equals("ALL")) {
            findArticle = articleRepository.findAll(pageable);
        } else {
            Category category = categoryRepository.findByTitle(categoryTitle).get();
            List<String> childCategoryTitles = category.getChild().stream().map(s -> s.getTitle()).collect(Collectors.toList());
            for (String childCategoryTitle : childCategoryTitles) {
                System.out.println("==============childCategoryTitle = " + childCategoryTitle);
            }
            findArticle = articleSearchRepository.findSearchArticle(categoryTitle, childCategoryTitles, pageable);

        }

        Page<ArticleCardBoxResponse> articleCardBoxResponses = findArticle.map(s -> ArticleCardBoxResponse.of(s));
        return articleCardBoxResponses;
    }

    public Slice<Article> findRecentArticle(int page) {
        Slice<Article> findRecentArticle = articleRepository.findByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE));

        return findRecentArticle;

    }

    /*
        서비스 로직
     */
    public List<SimpleArticle> findArticleListByCategory(Category category) {
        List<Article> findArticleListByCategory = articleRepository.findTop6ByCategoryOrderByCreatedDateDesc(category);
        return findArticleListByCategory.stream().map(
                s -> SimpleArticle.of(s)
        ).collect(Collectors.toList());
    }

    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(
                        () -> tagRepository.save(Tag.createTag(tagName))
                );
    }


    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);
        articleRepository.delete(article);
    }
}
