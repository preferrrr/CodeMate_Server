package com.example.codemate.dto;

import com.theokanning.openai.completion.CompletionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class GPTCompletionRequest {


    private String prompt;



    public static CompletionRequest of(GPTCompletionRequest restRequest) {
        return CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(restRequest.getPrompt())
                .maxTokens(1000)
                .build();
    }
}
