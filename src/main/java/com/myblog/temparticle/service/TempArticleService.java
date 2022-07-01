package com.myblog.temparticle.service;

import com.google.gson.Gson;
import com.myblog.temparticle.dto.TempArticleDto;
import com.myblog.temparticle.model.TempArticle;
import com.myblog.temparticle.repository.TempArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TempArticleService {
    private final TempArticleRepository tempArticleRepository;
    private final Gson gson;

    @Transactional
    public void saveTempArticle(TempArticleDto tempArticleDto) {

        String tags = extractedTag(tempArticleDto.getTag());

        TempArticle tempArticle = TempArticle.createTempArticle(
                tempArticleDto.getTitle(),
                tempArticleDto.getContent(),
                tempArticleDto.getThumbnailUrl(),
                tempArticleDto.getCategory(),
                tags
        );
        tempArticleRepository.save(tempArticle);
    }

    public TempArticleDto findAutoSavedArticle() {
        TempArticle tempArticle = tempArticleRepository.findById(1L).orElseGet(
                () -> new TempArticle()
        );

        return TempArticleDto.of(tempArticle);
    }

    @Transactional
    public void deleteAutoSavedArticle(Long tempArticleId) {
        tempArticleRepository.deleteById(tempArticleId);
    }

    public String extractedTag(String tagDto) {
        if (StringUtils.hasText(tagDto)) {
            List<Map<String, String>> tagsDtoArrayList = gson.fromJson(tagDto, ArrayList.class);
            List<String> tagList = tagsDtoArrayList.stream().map(s ->
                    s.get("value")
            ).collect(Collectors.toList());

            return String.join(",", tagList);
        }

        return "";
    }


}
