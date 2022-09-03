package com.myblog.comment.repository;

import com.myblog.comment.model.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.myblog.article.model.QArticle.article;
import static com.myblog.comment.model.QComment.comment;
import static com.myblog.member.model.QMember.member;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Comment> findCommentListByArticleId(Long articleId) {
        return queryFactory
                .selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.article.id.eq(articleId))
                .fetch();
    }

    public List<Comment> findRecentComment() {
        return queryFactory
                .selectFrom(comment)
                .join(comment.article, article).fetchJoin()
                .join(comment.member, member).fetchJoin()
                .limit(5)
                .fetch();
    }

    public Page<Comment> findAllComment(Pageable pageable) {
        List<Comment> content = getCommentList(pageable);
        Long count = getCommentCount();

        return new PageImpl<>(content, pageable, count);
    }

    private Long getCommentCount() {
        return queryFactory
                .select(comment.count())
                .from(comment)
                .fetchOne();
    }

    private List<Comment> getCommentList(Pageable pageable) {
        return queryFactory
                .selectFrom(comment)
                .join(comment.article, article).fetchJoin()
                .join(comment.member, member).fetchJoin()
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

}
