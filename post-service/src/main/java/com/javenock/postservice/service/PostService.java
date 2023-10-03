package com.javenock.postservice.service;

import com.javenock.postservice.exception.NoPostsAvailableException;
import com.javenock.postservice.exception.NoSuchPostException;
import com.javenock.postservice.model.Post;
import com.javenock.postservice.repository.PostRepository;
import com.javenock.postservice.request.PostRequest;
import com.javenock.postservice.response.CommentResponse;
import com.javenock.postservice.response.LikeResponse;
import com.javenock.postservice.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    RestTemplate restTemplate;

    public Post createNewPost(PostRequest postRequest) {
        Post newPost = Post.builder()
                .body(postRequest.getBody())
                .dateCreated(LocalDate.now())
                .userId(postRequest.getUserId())
                .build();
        return postRepository.save(newPost);
    }


    public Optional<PostResponse> fetchOptionalPost(Long postId) throws NoSuchPostException {
        ResponseEntity<CommentResponse[]> res_list = restTemplate.getForEntity("http://COMMENT-SERVICE:8083/comment/comments-post-id/{postId}", CommentResponse[].class, postId);
        List<CommentResponse> collect = Arrays.stream(res_list.getBody()).map(k -> CommentResponse.builder()
                .commentId(k.getCommentId())
                .commentBody(k.getCommentBody())
                .dateCommented(k.getDateCommented())
                .postId(k.getPostId())
                .userId(k.getUserId())
                .parentCommentId(k.getParentCommentId())
                .likeResponseList(k.getLikeResponseList())
                .childComments(k.getChildComments())
                .build()
        ).collect(Collectors.toList());
        Optional<Post> post = postRepository.findByPostId(postId);
        if(post.isPresent()){
            Optional<PostResponse> res = Optional.ofNullable(PostResponse.builder()
                    .postId(post.get().getPostId())
                    .body(post.get().getBody())
                    .dateCreated(post.get().getDateCreated())
                    .userId(post.get().getUserId())
                    .commentResponses(collect)
                    .build());
            return res;
        }else{
            throw new NoSuchPostException("No such post");
        }
    }


    public List<PostResponse> getAllPosts() throws NoPostsAvailableException {
        //post list
        List<Post> list_posts = postRepository.findAll();
        // comment list
        ResponseEntity<CommentResponse[]> res_list = restTemplate.getForEntity("http://COMMENT-SERVICE:8083/comment/comments-parents", CommentResponse[].class);
        List<CommentResponse> collect_comments = Arrays.stream(res_list.getBody()).map(k -> CommentResponse.builder()
                .commentId(k.getCommentId())
                .commentBody(k.getCommentBody())
                .dateCommented(k.getDateCommented())
                .postId(k.getPostId())
                .userId(k.getUserId())
                .parentCommentId(k.getParentCommentId())
                .likeResponseList(k.getLikeResponseList())
                .childComments(k.getChildComments())
                .build()
        ).collect(Collectors.toList());
        //likes list
        ResponseEntity<LikeResponse[]> res_list_likes = restTemplate.getForEntity("http://LIKE-SERVICE:8084/like/likes-by-post", LikeResponse[].class);
        List<LikeResponse> collect_likes = Arrays.stream(res_list_likes.getBody()).map(k -> LikeResponse.builder()
                .likeBody(k.getLikeBody())
                .likeId(k.getLikeId())
                .postId(k.getPostId())
                .userId(k.getUserId())
                .dateCreated(k.getDateCreated()).build()
        ).collect(Collectors.toList());
        if(list_posts.size() > 0){
            List<PostResponse> collect = list_posts.stream().map((post) -> PostResponse.builder()
                    .postId(post.getPostId())
                    .body(post.getBody())
                    .dateCreated(post.getDateCreated())
                    .userId(post.getUserId())
                    .commentResponses(collect_comments.stream().filter(k -> k.getPostId() == post.getPostId()).collect(Collectors.toList()))
                    .likeResponses(collect_likes.stream().filter(k -> k.getPostId() == post.getPostId()).collect(Collectors.toList()))
                    .build())
                    .collect(Collectors.toList());
            
            return collect;
        }else{
            throw new NoPostsAvailableException("Post List is empty");
        }
    }


    public Long updatePostById(PostRequest postRequest, Long postId) throws NoSuchPostException {
        Optional<Post> post = postRepository.findByPostId(postId);
        if (post.isPresent()){
            //update
            Post newPost = new Post();
                    newPost.setPostId(post.get().getPostId());
                    newPost.setBody(postRequest.getBody());
                    newPost.setDateCreated(post.get().getDateCreated());
                    newPost.setUserId(post.get().getUserId());
            return postRepository.save(newPost).getPostId();
        }else{
            // throw exception
            throw new NoSuchPostException("No such post to update");
        }
    }


    public void deleteByPostId(Long postId) throws NoSuchPostException {
        Optional<Post> post = postRepository.findByPostId(postId);
        if (post.isPresent()){
            postRepository.deleteById(postId);
        }else{
            // throw exception
            throw new NoSuchPostException("No such post to update");
        }
    }

    public boolean isPostPresent(Long postId){
        Optional<Post> post = postRepository.findByPostId(postId);
        if(post.isPresent()){
            return true;
        }else {
            return false;
        }
    }
}
