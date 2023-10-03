package com.javenock.postservice.exception;

public class NoPostsAvailableException extends Exception{
    public NoPostsAvailableException(String message) {
        super(message);
    }
}
