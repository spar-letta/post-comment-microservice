package com.javenock.likeservice.repository;

import com.javenock.likeservice.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "SELECT * FROM LIKES WHERE post_id = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    @Query(value = "SELECT * FROM LIKES WHERE comment_id = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Like> findByCommentIdAndUserId(Long commentId, Long userId);
}
