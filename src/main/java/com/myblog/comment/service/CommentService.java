package com.myblog.comment.service;

import com.myblog.article.model.Article;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.dto.CommentSaveRequest;
import com.myblog.comment.dto.ManageCommentResponse;
import com.myblog.comment.dto.RecentCommentResponse;
import com.myblog.comment.model.Comment;
import com.myblog.comment.repository.CommentQueryRepository;
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
    private final CommentQueryRepository commentQueryRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;


    /*
        - 게시물에 등록된 댓글 조회
     */
    public List<CommentListResponse> findCommentList(Long articleId) {

        Article article = articleRepository.findById(articleId).orElseThrow(NOT_FOUNT_ARTICLE::getException);

        return commentQueryRepository.findCommentListByArticleId(article.getId()).stream()
                .filter(comment -> comment.getParent() == null)
                .map(parentComment -> CommentListResponse.of(parentComment))
                .collect(Collectors.toList());
    }



    /*
        - 게시물에 댓글 등록
     */
    @Transactional
    @CacheEvict(value = "sideBarRecentCommentCaching", allEntries = true)
    public void saveComment(CustomOauth2User customOauth2User, CommentSaveRequest commentSaveRequest) {
        RightLoginChecker.checkLoginMember(customOauth2User);

        Member member = memberRepository.findById(customOauth2User.getMemberId()).orElseThrow(NOT_FOUND_MEMBER::getException);
        Article article = articleRepository.findById(commentSaveRequest.getArticleId()).orElseThrow(NOT_FOUNT_ARTICLE::getException);

        Comment parentComment = null;
        if (commentSaveRequest.checkParentCommentId()) {
            parentComment = commentRepository.findById(commentSaveRequest.getParentCommentId()).orElseThrow(NOT_FOUND_CATEGORY::getException);
        }

        commentRepository.save(
                Comment.createComment(
                        commentSaveRequest.getContent(),
                        commentSaveRequest.isSecret(),
                        article,
                        parentComment,
                        member
                ));
    }



    /*
        - 게시물에 등록된 댓글 삭제
     */
    @Transactional
    @CacheEvict(value = "sideBarRecentCommentCaching", allEntries = true)
    public void deleteComment(CustomOauth2User customOauth2User, Long commentId) {
        RightLoginChecker.checkLoginMember(customOauth2User);

        if (customOauth2User.getMemberRole().equals(Role.ADMIN)) {
            commentRepository.delete(commentRepository.findById(commentId).orElseThrow(NOT_FOUNT_COMMENT::getException));
        }
        if (customOauth2User.getMemberRole().equals(Role.USER)){
            commentRepository.delete(commentRepository.findCommentByIdAndMember_id(commentId, customOauth2User.getMemberId())
                    .orElseThrow(NOT_FOUNT_COMMENT::getException));
        }
    }



    /*
        - [관리자 페이지] 전체 댓글 조회
     */
    public Page<ManageCommentResponse> findAllComment(Pageable pageable, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);

        return commentQueryRepository.findAllComment(pageable)
                .map(ManageCommentResponse::of);
    }



    /*
        - 사이드바에 필요한 댓글 조회, 캐시 적용
     */
    @Cacheable(value = "sideBarRecentCommentCaching", key = "0")
    public List<RecentCommentResponse> findRecentComment() {

        return commentQueryRepository.findRecentComment().stream()
                .map(RecentCommentResponse::of)
                .collect(Collectors.toList());
    }

}
