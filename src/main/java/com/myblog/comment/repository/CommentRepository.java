package com.myblog.comment.repository;

import com.myblog.comment.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle_Id(Long id);

    List<Comment> findTop5ByOrderByIdDesc();

    @Query("select c from Comment c " +
            "join c.member " +
            "where c.id = :comment_id and c.member.id = :member_id")
    Optional<Comment> findCommentByIdAndMember_id(@Param("comment_id") Long comment_id, @Param("member_id") Long member_id);
}
