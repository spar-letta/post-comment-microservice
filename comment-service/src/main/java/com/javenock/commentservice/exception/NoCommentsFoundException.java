package com.javenock.commentservice.exception;

public class NoCommentsFoundException extends Exception{
    public NoCommentsFoundException(String message) {
        super(message);
    }
}
