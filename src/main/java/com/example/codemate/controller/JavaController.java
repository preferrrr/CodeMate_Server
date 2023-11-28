package com.example.codemate.controller;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.service.JavaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/java")
public class JavaController {

    private final JavaService javaService;

    @PostMapping("/run")
    public ResponseEntity<RunResponseDto> javaCompile(@RequestPart(name = "file") MultipartFile file,
                                         @RequestPart(name = "input", required = false) MultipartFile input) throws IOException {

        if(file.isEmpty())
            throw new IOException();

        RunResponseDto response = javaService.compile(file, input);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}



