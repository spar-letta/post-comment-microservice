package com.javenock.commentservice.repository;

import com.javenock.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentId(Long commentId);

    List<Comment> findByPostId(Long postId);

    List<Comment> findByParentCommentId(Long parentCommentId);
}
