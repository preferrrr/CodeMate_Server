package com.example.codemate.exception;

import com.example.codemate.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({NoInputValueException.class})
    public ResponseEntity<?> handleNoInputValueException(final NoInputValueException e) {
//
//        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
//        log.error(msg);

        ResultDto exceptionMessage = new ResultDto("입력값이 없습니다.");
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NO_CONTENT);
    }

}
