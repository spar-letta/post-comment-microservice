package com.javenock.likeservice.service;

import com.javenock.likeservice.likeRequest.LikeRequest;
import com.javenock.likeservice.model.Like;
import com.javenock.likeservice.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    LikeRepository likeRepository;


    public Like saveLikePost(LikeRequest likeRequest) {
        Optional<Like> like = likeRepository.findByPostIdAndUserId(likeRequest.getPostId(), likeRequest.getUserId());
        if(like.isPresent()){
            likeRepository.delete(like.get());
        }else{
            Like build= Like.builder()
                    .likeBody(likeRequest.getLikeBody())
                    .postId(likeRequest.getPostId())
                    .dateCreated(LocalDate.now())
                    .userId(likeRequest.getUserId())
                    .build();

            return likeRepository.save(build);
        }
        return null;
    }

    public Like saveLikeComment(LikeRequest likeRequest) {
        Optional<Like> like = likeRepository.findByCommentIdAndUserId(likeRequest.getCommentId(), likeRequest.getUserId());
        if(like.isPresent()){
            likeRepository.delete(like.get());
        }else{
            Like build= Like.builder()
                    .likeBody(likeRequest.getLikeBody())
                    .commentId(likeRequest.getCommentId())
                    .dateCreated(LocalDate.now())
                    .userId(likeRequest.getUserId())
                    .build();
            return likeRepository.save(build);
        }
        return null;
    }

    public List<Like> likesByPost() {
        List<Like> allByPost = likeRepository.findAll().stream().filter(k -> k.getPostId() != null).collect(Collectors.toList());
        return allByPost;
    }

    public List<Like> likesByComment() {
        List<Like> allByComment = likeRepository.findAll().stream().filter(k -> k.getPostId() == null).collect(Collectors.toList());
        return allByComment;
    }
}
