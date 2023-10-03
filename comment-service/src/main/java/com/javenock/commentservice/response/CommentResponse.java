package com.javenock.commentservice.response;

import com.javenock.commentservice.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Comment comment;
    private List<Comment> childCommentResponseList;

}
