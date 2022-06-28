package com.myblog.comment.service;

import com.myblog.article.model.Article;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.dto.CommentSaveRequest;
import com.myblog.article.exception.NotExistArticleException;
import com.myblog.comment.exception.NotExistCommentException;
import com.myblog.member.exception.NotExistMemberException;
import com.myblog.comment.model.Comment;
import com.myblog.comment.repository.CommentRepository;
import com.myblog.common.checker.RightLoginChecker;
import com.myblog.member.model.Member;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public List<CommentListResponse> findCommentList(Long articleId) {

        Article article = articleRepository.findById(articleId).orElseThrow(NotExistArticleException::new);

        List<Comment> findComments = commentRepository.findByArticle_Id(article.getId());

        List<Comment> parentComments = findComments.stream().filter(s -> s.getParent() == null)
                .collect(Collectors.toList());

        List<CommentListResponse> commentListResponses = parentComments.stream().map(s ->
                CommentListResponse.of(s, s.getMember())
        ).collect(Collectors.toList());

        return commentListResponses;
    }

    @Transactional
    public void saveComment(CustomOauth2User customOauth2User, CommentSaveRequest commentSaveRequest) {
        RightLoginChecker.checkLoginMember(customOauth2User);

        Member member = memberRepository.findById(customOauth2User.getMemberId()).orElseThrow(NotExistMemberException::new);
        Article article = articleRepository.findById(commentSaveRequest.getArticleId()).orElseThrow(NotExistArticleException::new);

        if (commentSaveRequest.checkParentCommentId()) {
            Comment parentComment = commentRepository.findById(commentSaveRequest.getParentCommentId()).orElseThrow(NotExistCommentException::new);
            commentRepository.save(
                    Comment.createComment(
                            commentSaveRequest.getContent(),
                            commentSaveRequest.isSecret(),
                            article,
                            parentComment,
                            member
                    )
            );
        } else {
            commentRepository.save(
                    Comment.createComment(
                            commentSaveRequest.getContent(),
                            commentSaveRequest.isSecret(),
                            article,
                            null,
                            member
                    )
            );
        }
    }

    @Transactional
    public void deleteComment(CustomOauth2User customOauth2User, Long commentId) {
        RightLoginChecker.checkLoginMember(customOauth2User);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotExistCommentException::new);
        commentRepository.delete(comment);
    }

}
