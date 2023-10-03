package com.javenock.commentservice.controller;

import com.javenock.commentservice.exception.CommentIdNotFoundException;
import com.javenock.commentservice.exception.FailedToSaveComment;
import com.javenock.commentservice.exception.NoCommentsFoundException;
import com.javenock.commentservice.model.Comment;
import com.javenock.commentservice.request.ChildCommentRequest;
import com.javenock.commentservice.request.CommentRequest;
import com.javenock.commentservice.response.ChildCommentResponse;
import com.javenock.commentservice.response.CommentResponse;
import com.javenock.commentservice.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/comment")
@RestController
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createNewComment(@RequestBody @Valid CommentRequest commentRequest) throws FailedToSaveComment {
        return commentService.createNewComment(commentRequest);
    }

    @PostMapping("/child-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createChildComment(@RequestBody @Valid ChildCommentRequest childCommentRequest) throws NoCommentsFoundException {
        return commentService.createChildComment(childCommentRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ChildCommentResponse> fetchAllComments() throws NoCommentsFoundException {
        return commentService.fetchAllComments();
    }

    @GetMapping("/comment-id/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse commentById(@PathVariable Long commentId) throws CommentIdNotFoundException {
        return commentService.fetchCommentById(commentId);
    }

    @GetMapping("/comments-post-id/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ChildCommentResponse> commentByPostId(@PathVariable Long postId) throws CommentIdNotFoundException, NoCommentsFoundException {
        return commentService.fetchCommentByPostId(postId);
    }

    //fetch comment child with parent comment id
    @GetMapping("/child-comments-parent-commentid/{parentCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> fetchChildComments(@PathVariable Long parentCommentId){
        return commentService.fetchChildComments(parentCommentId);
    }

    @GetMapping("/comments-parents")
    @ResponseStatus(HttpStatus.OK)
    public List<ChildCommentResponse> commentParentes(){
        return commentService.fetchCommentParents();
    }
}
