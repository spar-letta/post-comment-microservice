package com.javenock.commentservice.service;

import com.javenock.commentservice.exception.CommentIdNotFoundException;
import com.javenock.commentservice.exception.FailedToSaveComment;
import com.javenock.commentservice.exception.NoCommentsFoundException;
import com.javenock.commentservice.model.Comment;
import com.javenock.commentservice.request.ChildCommentRequest;
import com.javenock.commentservice.request.CommentRequest;
import com.javenock.commentservice.repository.CommentRepository;
import com.javenock.commentservice.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RestTemplate restTemplate;

    public Comment createNewComment(CommentRequest commentRequest) throws FailedToSaveComment {
        ResponseEntity<Boolean> res = restTemplate.getForEntity("http://POST-SERVICE:8082/post/is-post-present/{postId}", Boolean.class, commentRequest.getPostId());
       if(res.getBody().booleanValue() == true){
          //save comment
           Comment result = Comment.builder()
                   .commentBody(commentRequest.getCommentBody())
                   .dateCommented(LocalDate.now())
                   .postId(commentRequest.getPostId())
                   .userId(commentRequest.getUserId())
                   .build();
          return commentRepository.save(result);
       }else{
           throw new FailedToSaveComment("oops error in saving try again");
       }
    }

    public Comment createChildComment(ChildCommentRequest childCommentRequest) throws NoCommentsFoundException {
        Comment parentComment = commentRepository.findByCommentId(childCommentRequest.getParentCommentId()).orElseThrow(() -> new NoCommentsFoundException("No such comment with id :" + childCommentRequest.getParentCommentId()));
        Comment build = Comment.builder()
                .commentBody(childCommentRequest.getCommentBody())
                .parentCommentId(parentComment.getCommentId())
                .dateCommented(LocalDate.now())
                .userId(childCommentRequest.getUserId())
                .postId(childCommentRequest.getPostId())
                .build();
        return commentRepository.save(build);
    }

    public List<ChildCommentResponse> fetchAllComments() throws NoCommentsFoundException {
        ResponseEntity<LikeResponse[]> all_like_comments = restTemplate.getForEntity("http://LIKE-SERVICE:8084/like/likes-by-comment", LikeResponse[].class);
        List<LikeResponse> collect_likes_comments = Arrays.stream(all_like_comments.getBody()).map(like -> LikeResponse.builder()
                .likeBody(like.getLikeBody())
                .likeId(like.getLikeId())
                .userId(like.getUserId())
                .commentId(like.getCommentId())
                .dateCreated(like.getDateCreated())
                .build()
        ).collect(Collectors.toList());
        List<Comment> list = commentRepository.findAll();
        List<MtotoComment> collect_comments_with_likes = list.stream().map(r -> MtotoComment.builder()
                .commentBody(r.getCommentBody())
                .parentCommentId(r.getParentCommentId())
                .dateCommented(r.getDateCommented())
                .userId(r.getUserId())
                .postId(r.getPostId())
                .commentId(r.getCommentId())
                .likeResponseList(collect_likes_comments.stream().filter(k -> k.getCommentId() == r.getCommentId()).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());
            List<Comment> collect = list.stream().filter(comment -> comment.getParentCommentId() == null).collect(Collectors.toList());
        List<ChildCommentResponse> allComments = collect.stream().map(com ->
                ChildCommentResponse.builder()
                        .commentId(com.getCommentId())
                        .commentBody(com.getCommentBody())
                        .parentCommentId(com.getCommentId())
                        .dateCommented(com.getDateCommented())
                        .userId(com.getUserId())
                        .postId(com.getPostId())
                        .childComments(collect_comments_with_likes.stream().filter(comment -> comment.getParentCommentId() == com.getCommentId()).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
        return allComments;

    }

    public CommentResponse fetchCommentById(Long commentId) throws CommentIdNotFoundException {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new CommentIdNotFoundException("Comment id "+ commentId + "not found"));
        List<Comment> childComments = commentRepository.findByParentCommentId(commentId);
        CommentResponse build = CommentResponse.builder()
                .comment(comment)
                .childCommentResponseList(childComments)
                .build();
        return build;
    }

    public List<ChildCommentResponse> fetchCommentByPostId(Long postId) throws NoCommentsFoundException {
        ResponseEntity<LikeResponse[]> all_like_comments = restTemplate.getForEntity("http://LIKE-SERVICE:8084/like/likes-by-comment", LikeResponse[].class);
        List<LikeResponse> collect_likes_comments = Arrays.stream(all_like_comments.getBody()).map(like -> LikeResponse.builder()
                .likeBody(like.getLikeBody())
                .likeId(like.getLikeId())
                .userId(like.getUserId())
                .commentId(like.getCommentId())
                .dateCreated(like.getDateCreated())
                .build()
        ).collect(Collectors.toList());
        List<Comment> list = commentRepository.findByPostId(postId);
        List<MtotoComment> collect_comments_with_likes = list.stream().map(r -> MtotoComment.builder()
                .commentBody(r.getCommentBody())
                .parentCommentId(r.getParentCommentId())
                .dateCommented(r.getDateCommented())
                .userId(r.getUserId())
                .postId(r.getPostId())
                .commentId(r.getCommentId())
                .likeResponseList(collect_likes_comments.stream().filter(k -> k.getCommentId() == r.getCommentId()).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());
        List<Comment> collect = list.stream().filter(comment -> comment.getParentCommentId() == null).collect(Collectors.toList());
        List<ChildCommentResponse> allComments = collect.stream().map(com ->
                ChildCommentResponse.builder()
                        .commentId(com.getCommentId())
                        .commentBody(com.getCommentBody())
                        .parentCommentId(com.getCommentId())
                        .dateCommented(com.getDateCommented())
                        .userId(com.getUserId())
                        .postId(com.getPostId())
                        .childComments(collect_comments_with_likes.stream().filter(comment -> comment.getParentCommentId() == com.getCommentId()).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
        return allComments;
    }


    public List<Comment> fetchChildComments(Long parentCommentId) {
        List<Comment> childComments = commentRepository.findByParentCommentId(parentCommentId);
        return childComments;
    }

    public List<ChildCommentResponse> fetchCommentParents() {
        //fetch all likes as per comments
        ResponseEntity<LikeResponse[]> all_like_comments = restTemplate.getForEntity("http://LIKE-SERVICE:8084/like/likes-by-comment", LikeResponse[].class);
        List<LikeResponse> collect_likes_comments = Arrays.stream(all_like_comments.getBody()).map(like -> LikeResponse.builder()
                .likeBody(like.getLikeBody())
                .likeId(like.getLikeId())
                .userId(like.getUserId())
                .commentId(like.getCommentId())
                .dateCreated(like.getDateCreated())
                .build()
        ).collect(Collectors.toList());

        List<Comment> list = commentRepository.findAll();

        List<MtotoComment> collect_comments_with_likes = list.stream().map(r -> MtotoComment.builder()
                .commentBody(r.getCommentBody())
                .parentCommentId(r.getParentCommentId())
                .dateCommented(r.getDateCommented())
                .userId(r.getUserId())
                .postId(r.getPostId())
                .commentId(r.getCommentId())
                .likeResponseList(collect_likes_comments.stream().filter(k -> k.getCommentId() == r.getCommentId()).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());

        List<MtotoComment> collect = collect_comments_with_likes.stream().filter(comment -> comment.getParentCommentId() == null).collect(Collectors.toList());
        List<ChildCommentResponse> allComments = collect.stream().map(com ->
                ChildCommentResponse.builder()
                        .commentId(com.getCommentId())
                        .commentBody(com.getCommentBody())
                        .parentCommentId(com.getCommentId())
                        .dateCommented(com.getDateCommented())
                        .userId(com.getUserId())
                        .postId(com.getPostId())
                        .likeResponseList(com.getLikeResponseList())
                        .childComments(collect_comments_with_likes.stream().filter(comment -> comment.getParentCommentId() == com.getCommentId()).collect(Collectors.toList())
                        )
                        .build()
        ).collect(Collectors.toList());



        return allComments;
    }
}
