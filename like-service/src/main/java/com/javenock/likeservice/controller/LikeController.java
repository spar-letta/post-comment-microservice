package com.javenock.likeservice.controller;

import com.javenock.likeservice.likeRequest.LikeRequest;
import com.javenock.likeservice.model.Like;
import com.javenock.likeservice.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/like")
@RestController
public class LikeController {

    private LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like-post")
    @ResponseStatus(HttpStatus.OK)
    public Like saveLikePost(@RequestBody LikeRequest likeRequest){
        return likeService.saveLikePost(likeRequest);
    }

    @PostMapping("/like-comment")
    @ResponseStatus(HttpStatus.OK)
    public Like saveLikeComment(@RequestBody LikeRequest likeRequest){
        return likeService.saveLikeComment(likeRequest);
    }

    @GetMapping("/likes-by-post")
    @ResponseStatus(HttpStatus.OK)
    public List<Like> likesByPost(){
        return likeService.likesByPost();
    }

    @GetMapping("/likes-by-comment")
    @ResponseStatus(HttpStatus.OK)
    public List<Like> likesByComment(){
        return likeService.likesByComment();
    }
}
