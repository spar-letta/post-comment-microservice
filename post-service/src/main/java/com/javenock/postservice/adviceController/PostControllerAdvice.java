package com.javenock.postservice.adviceController;

import com.javenock.postservice.exception.NoPostsAvailableException;
import com.javenock.postservice.exception.NoSuchPostException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PostControllerAdvice {

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
    @ExceptionHandler(NoPostsAvailableException.class)
    public Map<String, String> handleNoPostsAvailableException(NoPostsAvailableException exception){
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System message :", exception.getMessage());
        return mapError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchPostException.class)
    public Map<String, String> handleNoSuchPostException(NoSuchPostException exception){
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System message :", exception.getMessage());
        return mapError;
    }


}
