package com.basak.dalcom.external_api.openai.service.dto;

import com.basak.dalcom.external_api.openai.controller.dto.OpenAIRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CompletionAPIRespDto {

    private final String id;
    private final Integer created;
    private final List<Choice> choices;

    public CompletionAPIRespDto(Map<String, Object> respBody) {
        this.id = (String) respBody.get("id");
        this.created = (Integer) respBody.get("created");

        this.choices = new ArrayList<>();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) respBody.get("choices");
        for (Map<String, Object> rawChoice : choices) {
            Map<String, String> rawMessage = (Map<String, String>) rawChoice.get("message");
            Message message = Message.builder()
                .role(OpenAIRole.enumOf(rawMessage.get("role")).get())
                .content(rawMessage.get("content"))
                .build();

            Choice choice = Choice.builder()
                .index((Integer) rawChoice.get("index"))
                .finishReason((String) rawChoice.get("finish_reason"))
                .message(message)
                .build();

            this.choices.add(choice);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Message {

        private OpenAIRole role;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Choice {

        private Integer index;
        private Message message;
        private String finishReason;
    }
}
