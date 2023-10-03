package com.javenock.commentservice.response;

import com.javenock.commentservice.model.Comment;
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
public class ParentCommentResponse {

    private List<ChildCommentResponse> childCommentResponse;
}
