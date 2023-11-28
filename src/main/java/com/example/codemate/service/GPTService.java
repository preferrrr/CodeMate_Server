package com.example.codemate.service;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.dto.CompletionResponse;
import com.example.codemate.dto.GPTCompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final OpenAiService openAiService;

    public RunResponseDto completion(final GPTCompletionRequest restRequest) {
        restRequest.setPrompt("프로그래밍 코드에 대한 질문할거야.\n" + restRequest.getPrompt()+ "\n한국말로 답해줘. 그리고 답변의 시작에 줄바꿈 넣지마.");
        CompletionResult result = openAiService.createCompletion(GPTCompletionRequest.of(restRequest));
        CompletionResponse response = CompletionResponse.of(result);

        List<String> messages = response.getMessages().stream()
                .map(CompletionResponse.Message::getText)
                .collect(Collectors.toList());

        String message = messages.get(0);
        RunResponseDto dto = RunResponseDto.builder().result(message).build();

        return dto;
    }


    public RunResponseDto ocr(final GPTCompletionRequest restRequest) {

        restRequest.setPrompt(restRequest.getPrompt()+ "\n 코드에 오류가 있으면 수정해서 실행되도록 하고, 코드를 \\n과 \\t를 사용해서 줄바꿈과 들여쓰기 문자도 넣어줘. 코드만 보여줘.");

        CompletionResult result = openAiService.createCompletion(GPTCompletionRequest.of(restRequest));

        CompletionResponse response = CompletionResponse.of(result);

        List<String> messages = response.getMessages().stream()
                .map(CompletionResponse.Message::getText)
                .collect(Collectors.toList());

        RunResponseDto dto = RunResponseDto.builder().result(messages.get(0)).build();


        return dto;
    }


}
