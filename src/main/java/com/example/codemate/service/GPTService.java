package com.example.codemate.service;

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

    public CompletionResponse completion(final GPTCompletionRequest restRequest) {
        CompletionResult result = openAiService.createCompletion(GPTCompletionRequest.of(restRequest));
        CompletionResponse response = CompletionResponse.of(result);

        List<String> messages = response.getMessages().stream()
                .map(CompletionResponse.Message::getText)
                .collect(Collectors.toList());

        return response;
    }


}
