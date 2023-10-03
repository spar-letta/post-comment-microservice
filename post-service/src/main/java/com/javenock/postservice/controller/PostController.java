package com.javenock.postservice.controller;

import com.javenock.postservice.exception.NoPostsAvailableException;
import com.javenock.postservice.exception.NoSuchPostException;
import com.javenock.postservice.model.Post;
import com.javenock.postservice.request.PostRequest;
import com.javenock.postservice.response.CommentResponse;
import com.javenock.postservice.response.PostResponse;
import com.javenock.postservice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/post")
@RestController
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody @Valid PostRequest postRequest){
        return postService.createNewPost(postRequest);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<PostResponse> fetchSinglePost(@PathVariable Long postId) throws NoSuchPostException {
        return postService.fetchOptionalPost(postId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> getAllPosts() throws NoPostsAvailableException {
        return postService.getAllPosts();
    }

    @PutMapping("/update/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Long updatePost(@RequestBody @Valid PostRequest postRequest, @PathVariable Long postId) throws NoSuchPostException {
        return postService.updatePostById(postRequest, postId);
    }

    @DeleteMapping("/delete/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable Long postId) throws NoSuchPostException {
        postService.deleteByPostId(postId);
    }

    @GetMapping("/is-post-present/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isPostPresent(@PathVariable Long postId){
        return postService.isPostPresent(postId);
    }
}
