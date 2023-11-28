package com.example.codemate.exception;

import lombok.Getter;

public class FileSaveException extends RuntimeException{
    @Getter
    private final String NAME;

    public FileSaveException() {
        NAME = "FileSaveException";
    }
}
