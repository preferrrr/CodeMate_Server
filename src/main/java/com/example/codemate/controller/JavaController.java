package com.example.codemate.controller;

import com.example.codemate.dto.ResultDto;
import com.example.codemate.service.JavaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/java")
public class JavaController {

    private final JavaService javaService;

    @PostMapping("/compile")
    public ResponseEntity<?> javaCompile(@RequestPart(name = "file") MultipartFile file,
                                         @RequestPart(name = "input", required = false) MultipartFile input) throws IOException {

        if(file.isEmpty())
            throw new IOException();

        String result = javaService.compile(file, input);
        ResultDto response = new ResultDto(result);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}



