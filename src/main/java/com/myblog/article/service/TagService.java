package com.myblog.article.service;

import com.myblog.article.dto.TagResponse;
import com.myblog.article.model.Tag;
import com.myblog.article.repository.TagRepository;
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

        List<TagResponse> tagList = tagRepository.findAll().stream()
                .map(Tag::getName)
                .map(TagResponse::of)
                .collect(Collectors.toList());

        return tagList;
    }



}
