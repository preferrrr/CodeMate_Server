package com.example.codemate.controller;

import com.example.codemate.service.GccService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/c")
@RequiredArgsConstructor
public class GccController {

    private final GccService gccService;

    @PostMapping("/compile")
    public ResponseEntity<?> gccCompile(@RequestPart MultipartFile file) throws IOException {
        if(file.isEmpty())
            throw new IOException();

        String response = gccService.compileC(file);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
