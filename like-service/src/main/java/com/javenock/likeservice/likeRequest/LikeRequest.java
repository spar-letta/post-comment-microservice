package com.javenock.likeservice.likeRequest;

import lombok.Data;


@Data
public class LikeRequest {
    public String likeBody;
    public Long postId;
    public Long commentId;
    public Long userId;
}
