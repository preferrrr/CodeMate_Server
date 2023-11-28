package com.example.codemate.exception;

import lombok.Getter;

public class InfiniteLoopException extends RuntimeException{
    @Getter
    private final String NAME;

    public InfiniteLoopException() {
        NAME = "InfiniteLoopException";
    }
}
