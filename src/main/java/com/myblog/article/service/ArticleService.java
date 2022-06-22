package com.myblog.article.service;

import com.google.gson.Gson;
import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.model.Article;
import com.myblog.article.model.ArticleTag;
import com.myblog.article.model.Tag;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.article.repository.ArticleTagRepository;
import com.myblog.article.repository.TagRepository;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.member.repository.MemberRepository;
import com.myblog.security.oauth2.CustomOauth2User;
import lombok.RequiredArgsConstructor;
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
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;
    private final Gson gson;


    @Transactional
    public void writeArticle(ArticleWriteDto articleWriteDto, CustomOauth2User customOauth2User) {

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

    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(
                        () -> tagRepository.save(Tag.createTag(tagName))
                );
    }
}
