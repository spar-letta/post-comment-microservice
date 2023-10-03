package com.javenock.postservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long postId;
    private String body;
    private LocalDate dateCreated;
    private Long userId;
    private List<CommentResponse> commentResponses;
    private List<LikeResponse> likeResponses;
}
