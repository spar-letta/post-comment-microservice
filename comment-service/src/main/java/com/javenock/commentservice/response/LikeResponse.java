package com.javenock.commentservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    private Long likeId;
    private String likeBody;
    private LocalDate dateCreated;
    private Long commentId;
    private Long userId;
}
