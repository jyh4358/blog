package com.myblog.comment.service;

import com.myblog.article.model.Article;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.dto.CommentSaveRequest;
import com.myblog.comment.dto.ManageCommentResponse;
import com.myblog.comment.dto.RecentCommentResponse;
import com.myblog.comment.model.Comment;
import com.myblog.comment.repository.CommentRepository;
import com.myblog.common.checker.RightLoginChecker;
import com.myblog.member.model.Member;
import com.myblog.member.model.Role;
import com.myblog.member.repository.MemberRepository;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.myblog.common.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;


    public List<CommentListResponse> findCommentList(Long articleId) {

        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);
        List<Comment> findComments = commentRepository.findByArticle_Id(article.getId());

        return findComments.stream()
                .filter(s -> s.getParent() == null)
                .map(parentComment -> CommentListResponse.of(parentComment, parentComment.getMember()))
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "sideBarRecentCommentCaching", allEntries = true)
    public void saveComment(CustomOauth2User customOauth2User, CommentSaveRequest commentSaveRequest) {
        RightLoginChecker.checkLoginMember(customOauth2User);

        Member member = memberRepository.findById(customOauth2User.getMemberId()).orElseThrow(NOT_FOUND_MEMBER::getException);
        Article article = articleRepository.findById(commentSaveRequest.getArticleId()).orElseThrow(NOT_FOUNT_ARTICLE::getException);

        if (commentSaveRequest.checkParentCommentId()) {
            Comment parentComment = commentRepository.findById(commentSaveRequest.getParentCommentId()).orElseThrow(NOT_FOUND_CATEGORY::getException);
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

    public Page<ManageCommentResponse> findAllComment(Pageable pageable, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);
        Page<Comment> findCommentList = commentRepository.findAll(pageable);
        return findCommentList.map(
                findComment -> ManageCommentResponse.of(findComment)
        );
    }

    @Transactional
    @CacheEvict(value = "sideBarRecentCommentCaching", allEntries = true)
    public void deleteComment(CustomOauth2User customOauth2User, Long commentId) {
        RightLoginChecker.checkLoginMember(customOauth2User);

        if (customOauth2User.getMemberRole().equals(Role.ADMIN)) {
            commentRepository.delete(
                    commentRepository.findById(commentId).orElseThrow(NOT_FOUNT_COMMENT::getException)
            );
        }
        if (customOauth2User.getMemberRole().equals(Role.USER)){
            commentRepository.delete(
                    commentRepository.findCommentByIdAndMember_id(commentId, customOauth2User.getMemberId()).orElseThrow(NOT_FOUNT_COMMENT::getException)
            );
        }
    }

    @Cacheable(value = "sideBarRecentCommentCaching", key = "0")
    public List<RecentCommentResponse> findRecentComment() {
        List<Comment> recentTop5ByOrderByIdDesc = commentRepository.findTop5ByOrderByIdDesc();

        return commentRepository.findTop5ByOrderByIdDesc().stream()
                .map(recentComment -> RecentCommentResponse.of(
                        recentComment.getArticle().getId(),
                        recentComment.getContent(),
                        recentComment.getMember().getId(),
                        recentComment.getMember().getUsername(),
                        recentComment.isSecret()))
                .collect(Collectors.toList());
    }

}
