package com.myblog.comment.dto;

import com.myblog.comment.model.Comment;
import com.myblog.member.model.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentListResponse {
    private Long id;
    private String content;
    private Long memberId;
    private String username;
    private String userPictureUrl;
    private boolean secret;
    private LocalDateTime createDate;

    private List<ChildComment> childComments = new ArrayList<>();

    public static CommentListResponse of(Comment comment) {
        return new CommentListResponse(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getId(),
                comment.getMember().getUsername(),
                comment.getMember().getPicUrl(),
                comment.isSecret(),
                comment.getCreatedDate(),
                comment.getChild().stream()
                        .map(ChildComment::of)
                        .collect(Collectors.toList())
        );
    }
}
