package com.javenock.commentservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MtotoComment {
    private Long commentId;
    private String commentBody;
    private LocalDate dateCommented;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
    private List<LikeResponse> likeResponseList;
}
