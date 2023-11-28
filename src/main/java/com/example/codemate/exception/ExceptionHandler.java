package com.example.codemate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({NoInputValueException.class})
    public ResponseEntity<?> handleNoInputValueException(final NoInputValueException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";

        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //input이 필요한데 없음
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({FileSaveException.class})
    public ResponseEntity<?> handleNoFileSaveException(final FileSaveException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";

        log.error(msg);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //파일 저장 실패
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InfiniteLoopException.class})
    public ResponseEntity<?> handleNoInfiniteLoopException(final InfiniteLoopException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";

        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //무한 루프
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({RunException.class})
    public ResponseEntity<?> handleNoRunException(final RunException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";

        log.error(msg);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //코드 실행 실패
    }
}
