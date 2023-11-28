package com.example.codemate.exception;

import lombok.Getter;

public class RunException extends RuntimeException{
    @Getter
    private final String NAME;

    public RunException() {
        NAME = "RunException";
    }
}
