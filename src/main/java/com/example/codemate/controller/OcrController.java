package com.example.codemate.controller;

import com.example.codemate.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/ocr")
@Controller
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;
    @PostMapping("")
    public ResponseEntity<String> OCR(@RequestPart MultipartFile file) throws IOException {

        String result = ocrService.ocr(file);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
