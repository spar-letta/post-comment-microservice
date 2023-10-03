package com.javenock.likeservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Table(name = "LIKES")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    private String likeBody;
    private LocalDate dateCreated;
    private Long postId;
    private Long commentId;
    private Long userId;
}
