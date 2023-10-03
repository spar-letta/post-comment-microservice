package com.javenock.commentservice.exception;

public class FailedToSaveComment extends Exception{
    public FailedToSaveComment(String message) {
        super(message);
    }
}
