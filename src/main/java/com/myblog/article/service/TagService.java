package com.myblog.article.service;

import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.dto.TagResponse;
import com.myblog.article.model.Article;
import com.myblog.article.model.Tag;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.article.repository.TagRepository;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.member.repository.MemberRepository;
import com.myblog.security.oauth2.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;


    public List<TagResponse> findAllTag() {

        List<Tag> findTags = tagRepository.findAll();
        List<TagResponse> tagList = findTags.stream().map(s ->
                TagResponse.of(s.getName())
        ).collect(Collectors.toList());

        for (TagResponse tagResponse : tagList) {
            System.out.println("tagResponse = " + tagResponse.getName());
        }

        return tagList;
    }



}
