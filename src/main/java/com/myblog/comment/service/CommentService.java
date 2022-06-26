package com.myblog.comment.service;

import com.myblog.article.model.Article;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.exception.NotExistArticleException;
import com.myblog.comment.model.Comment;
import com.myblog.comment.repository.CommentRepository;
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


}
