package com.example.codemate.controller;

import com.example.codemate.tesseract.OCR;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
@Controller
public class tesseractController {

    @GetMapping("/ocr")
    public ResponseEntity<?> OCRtest() {
        String result = OCR.ocrImage("test.png");
        System.out.println(result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
