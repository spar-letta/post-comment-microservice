package com.javenock.commentservice.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@Data
public class ChildCommentRequest {
    @NotBlank
    private String commentBody;
    @Min(1)
    private Long postId;
    @Min(1)
    private Long userId;
    @Min(1)
    private Long parentCommentId;
}
