package com.myblog.comment;

import com.myblog.article.dto.ArticleWriteRequest;
import com.myblog.article.model.Article;
import com.myblog.article.model.ArticleTag;
import com.myblog.article.model.Tag;
import com.myblog.category.model.Category;
import com.myblog.commen.TestHelper;
import com.myblog.commen.basetest.ServiceBaseTest;
import com.myblog.comment.dto.CommentSaveRequest;
import com.myblog.comment.model.Comment;
import com.myblog.comment.service.CommentService;
import com.myblog.common.exception.ExceptionMessage;
import com.myblog.member.model.Member;
import com.myblog.security.oauth2.model.CustomOauth2User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentServiceTest extends ServiceBaseTest {
    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("댓글 작성")
    @Transactional
    public void 댓글_작성() {
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

        CommentSaveRequest commentSaveRequest = new CommentSaveRequest(savedArticle.getId(), "댓글작성", false, null);

        // when
        commentService.saveComment(customOauth2User, commentSaveRequest);

        // then
        Comment savedComment = commentRepository.findAll().get(0);
        Assertions.assertThat(savedComment.getContent()).isEqualTo(commentSaveRequest.getContent());
        Assertions.assertThat(savedComment.getArticle().getId()).isEqualTo(commentSaveRequest.getArticleId());
    }

    @Test
    @DisplayName("댓글 삭제")
    @Transactional
    public void 댓글_삭제() {
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

        Comment savedComment = commentRepository.save(Comment.createComment("댓글 내용", false, savedArticle, null, member));

        // when
        commentService.deleteComment(customOauth2User, savedComment.getId());

        // then
        assertThrows(ExceptionMessage.NOT_FOUNT_COMMENT.getException().getClass(), () ->
                commentRepository.findById(savedComment.getId()).orElseThrow(ExceptionMessage.NOT_FOUNT_COMMENT::getException));
    }
}
