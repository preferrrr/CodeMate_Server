package com.example.codemate.controller;

import com.example.codemate.dto.CompileResponseDto;
import com.example.codemate.service.GccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/c")
@RequiredArgsConstructor
@Tag(name = "C, C++ 관련 API")
public class GccController {

    private final GccService gccService;


    @PostMapping("/compile")
    @Operation(summary = "C,C++ 컴파일 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "컴파일 완료",
                    content = @Content(schema = @Schema(implementation = CompileResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "input 파일 없음."),
            @ApiResponse(responseCode = "500", description = "서버 에러(파일 저장 실패 등,,)")
    })
    public ResponseEntity<?> gccCompile(@Schema(name = "컴파일할 파일") @RequestPart(name = "file") MultipartFile file,
                                        @Schema(name = "사용자 입력 파일") @RequestPart(name = "input", required = false) MultipartFile input) throws IOException {

        if (file.isEmpty())
            throw new IOException();

        CompileResponseDto response = new CompileResponseDto(gccService.compile(file, input));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
