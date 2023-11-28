package com.example.codemate.controller;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.dto.GPTCompletionRequest;
import com.example.codemate.service.GPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;

    @PostMapping("/question")
    public ResponseEntity<RunResponseDto> completion(final @RequestBody GPTCompletionRequest gptCompletionRequest) {

        RunResponseDto result = gptService.completion(gptCompletionRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/ocr")
    public ResponseEntity<RunResponseDto> ocr(@RequestBody GPTCompletionRequest gptCompletionRequest) {

        RunResponseDto result = gptService.ocr(gptCompletionRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
