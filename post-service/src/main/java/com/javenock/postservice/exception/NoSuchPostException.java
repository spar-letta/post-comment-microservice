package com.javenock.postservice.exception;

public class NoSuchPostException extends Exception{
    public NoSuchPostException(String message) {
        super(message);
    }
}
