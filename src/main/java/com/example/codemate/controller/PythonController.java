package com.example.codemate.controller;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.service.PythonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/python")
public class PythonController {

    private final PythonService pythonService;

    @PostMapping("/run")
    public ResponseEntity<RunResponseDto> pythonCompile(@RequestPart(name = "file") MultipartFile file,
                                           @RequestPart(name = "input", required = false) MultipartFile input) throws IOException {

        if (file.isEmpty())
            throw new IOException();

        RunResponseDto response = pythonService.compile(file, input);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
