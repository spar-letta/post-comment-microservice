package com.javenock.commentservice.advice;

import com.javenock.commentservice.exception.CommentIdNotFoundException;
import com.javenock.commentservice.exception.FailedToSaveComment;
import com.javenock.commentservice.exception.NoCommentsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommentAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FailedToSaveComment.class)
    public Map<String, String> handleNoPostsAvailableException(FailedToSaveComment exception){
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System message :", exception.getMessage());
        return mapError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoCommentsFoundException.class)
    public Map<String, String> handleNoCommentsFoundException(NoCommentsFoundException exception){
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System message :", exception.getMessage());
        return mapError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CommentIdNotFoundException.class)
    public Map<String, String> handleCommentIdNotFoundException(CommentIdNotFoundException exception){
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System message :", exception.getMessage());
        return mapError;
    }
}
