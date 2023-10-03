package com.javenock.postservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long commentId;
    private String commentBody;
    private LocalDate dateCommented;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
}
