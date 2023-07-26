package com.example.codemate.controller;

import com.example.codemate.dto.ResultDto;
import com.example.codemate.service.PythonService;
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
@RequestMapping("/python")
public class PythonController {

    private final PythonService pythonService;

    @PostMapping("/compile")
    public ResponseEntity<?> pythonCompile(@RequestPart(name = "file") MultipartFile file,
                                           @RequestPart(name = "input") MultipartFile input) throws IOException {

        if (file.isEmpty())
            throw new IOException();

        ResultDto response = new ResultDto(pythonService.compile(file, input));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
