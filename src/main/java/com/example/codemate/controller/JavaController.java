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
    public ResponseEntity<?> javaCompile(@RequestPart MultipartFile file) throws IOException {

        if(file.isEmpty())
            throw new IOException();

        ResultDto response = new ResultDto(javaService.compile(file));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}



