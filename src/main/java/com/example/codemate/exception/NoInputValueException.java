package com.example.codemate.exception;

import lombok.Getter;

public class NoInputValueException extends RuntimeException{
    @Getter
    private final String NAME;

    public NoInputValueException() {
        NAME = "NoInputValueException";
    }
}
