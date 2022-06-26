package com.myblog.comment.dto;

import com.myblog.comment.model.Comment;
import com.myblog.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentListResponse {
    private Long id;
    private String content;
    private String username;
    private String userPictureUrl;
    private boolean secret;
    private LocalDateTime localDateTime;

    private List<ChildComment> childComments = new ArrayList<>();

    public static CommentListResponse of(Comment comment, Member member) {
        return new CommentListResponse(
                comment.getId(),
                comment.getContent(),
                member.getUsername(),
                member.getPicUrl(),
                comment.isSecret(),
                comment.getCreatedDate(),
                comment.getChild().stream().map(
                        t -> ChildComment.of(t, t.getMember())
                ).collect(Collectors.toList())
        );
    }
}
