package com.myblog.article;

import com.myblog.article.dto.ArticleDetailResponse;
import com.myblog.article.dto.ArticleWriteRequest;
import com.myblog.article.model.Article;
import com.myblog.article.model.ArticleTag;
import com.myblog.article.model.Tag;
import com.myblog.article.service.ArticleService;
import com.myblog.category.model.Category;
import com.myblog.commen.TestHelper;
import com.myblog.commen.basetest.ServiceBaseTest;
import com.myblog.member.model.Member;
import com.myblog.security.oauth2.model.CustomOauth2User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ArticleServiceTest extends ServiceBaseTest {

    @Autowired
    private ArticleService articleService;

    @Test
    @Transactional
    @DisplayName("게시물 작성")
    public void 게시물_작성() {
        // given
        Member member = TestHelper.관리자_생성(memberRepository);
        CustomOauth2User customOauth2User = TestHelper.인증_객체_생성(member);

        Category category = categoryRepository.save(new Category("Java"));
        ArticleWriteRequest articleWriteRequest = new ArticleWriteRequest(
                "게시물 타이틀",
                "게시물 내용",
                "게시물 썸내일",
                category.getId(),
                "[{\"value\":\"Java\"},{\"value\":\"Study\"}]"
        );
        // when
        Article article = articleService.writeArticle(articleWriteRequest, customOauth2User);

        // then
        Assertions.assertThat(article.getTitle()).isEqualTo(articleWriteRequest.getTitle());
        Assertions.assertThat(article.getContent()).isEqualTo(articleWriteRequest.getContent());
        Assertions.assertThat(article.getThumbnailUrl()).isEqualTo(articleWriteRequest.getThumbnailUrl());
        Assertions.assertThat(article.getCategory().getId()).isEqualTo(articleWriteRequest.getCategory());
        Assertions.assertThat(article.getArticleTags().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("게시물 상세 조회")
    public void 게시물_상세_조회() {
        // given
        Member member = TestHelper.관리자_생성(memberRepository);
        CustomOauth2User customOauth2User = TestHelper.인증_객체_생성(member);

        Category savedCategory = categoryRepository.save(new Category("Java"));

        Tag savedTag = tagRepository.save(new Tag("java"));
        ArticleWriteRequest articleWriteRequest = new ArticleWriteRequest(
                "게시물 타이틀",
                "게시물 내용",
                "게시물 썸내일",
                savedCategory.getId(),
                "[{\"value\":\"Java\"},{\"value\":\"Study\"}]"
        );
        ArticleTag savedArticleTag = new ArticleTag(savedTag);
        Article savedArticle = articleRepository.save(Article.createArticle(articleWriteRequest, customOauth2User.getMember(), savedCategory, List.of(savedArticleTag)));

        // when
        ArticleDetailResponse articleDetail = articleService.findArticleDetail(savedArticle.getId(), false);

        // then
        Assertions.assertThat(articleDetail.getId()).isEqualTo(savedArticle.getId());
        Assertions.assertThat(articleDetail.getTitle()).isEqualTo(savedArticle.getTitle());
        Assertions.assertThat(articleDetail.getHit()).isEqualTo(0L);
        Assertions.assertThat(articleDetail.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(articleDetail.getThumbnailUrl()).isEqualTo(savedArticle.getThumbnailUrl());
        Assertions.assertThat(articleDetail.getCategory()).isEqualTo(savedCategory.getTitle());
        Assertions.assertThat(articleDetail.getTags().size()).isEqualTo(1);
        Assertions.assertThat(articleDetail.getTags().get(0)).isEqualTo(savedTag.getName());

    }

    @Test
    @Transactional
    @DisplayName("게시물 수정")
    public void 게시물_수정_() {
        // given
        Member member = TestHelper.관리자_생성(memberRepository);
        CustomOauth2User customOauth2User = TestHelper.인증_객체_생성(member);

        Category savedCategory = categoryRepository.save(new Category("Java"));

        Tag savedTag = tagRepository.save(new Tag("java"));
        ArticleWriteRequest articleWriteRequest = new ArticleWriteRequest(
                "게시물 타이틀",
                "게시물 내용",
                "게시물 썸내일",
                savedCategory.getId(),
                "[{\"value\":\"Java\"},{\"value\":\"Study\"}]"
        );
        ArticleTag savedArticleTag = new ArticleTag(savedTag);
        Article savedArticle = articleRepository.save(Article.createArticle(articleWriteRequest, customOauth2User.getMember(), savedCategory, List.of(savedArticleTag)));

        ArticleWriteRequest modifyDto = new ArticleWriteRequest(
                "수정된 타이틀",
                "수정된 내용",
                "수정된 썸내일",
                savedCategory.getId(),
                "[{\"value\":\"Java\"},{\"value\":\"Study\"}]"
        );
        // when
        Long articleId = articleService.modifyArticle(savedArticle.getId(), modifyDto, customOauth2User);

        // then
        Article modifiedArticle = articleRepository.findById(articleId).get();
        Assertions.assertThat(modifiedArticle.getId()).isEqualTo(articleId);
        Assertions.assertThat(modifiedArticle.getTitle()).isEqualTo(modifyDto.getTitle());
        Assertions.assertThat(modifiedArticle.getThumbnailUrl()).isEqualTo(modifyDto.getThumbnailUrl());
        Assertions.assertThat(modifiedArticle.getCategory().getId()).isEqualTo(modifyDto.getCategory());
    }

}
