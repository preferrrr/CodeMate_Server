package com.example.codemate.controller;

import com.example.codemate.dto.CompileResponseDto;
import com.example.codemate.tesseract.OCR;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/test")
@Controller
@Tag(name = "OCR 관련 API")
public class tesseractController {

    @GetMapping("/ocr")
    @Operation(summary = "이미지 문자 인식")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CompileResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "파일 없음."),
            @ApiResponse(responseCode = "500", description = "서버 에러(파일 저장 실패 등,,)")
    })
    public ResponseEntity<?> OCRtest(@RequestPart MultipartFile file) {
        String result = OCR.ocrImage("test.png");
        System.out.println(result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
