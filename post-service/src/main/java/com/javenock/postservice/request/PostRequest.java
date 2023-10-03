package com.javenock.postservice.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class PostRequest {
    @NotBlank
    private String body;
    private LocalDate dateCreated;
    @Min(1)
    private Long userId;
}
