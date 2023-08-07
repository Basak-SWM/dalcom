package com.basak.dalcom.external_api.openai.service;

import com.basak.dalcom.config.OpenAIConfig;
import com.basak.dalcom.domain.common.exception.UnhandledException;
import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import com.basak.dalcom.domain.core.speech.data.AIChatLogRepository;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.dto.AIChatLogRetrieveResult;
import com.basak.dalcom.external_api.common.service.APIServiceImpl;
import com.basak.dalcom.external_api.openai.controller.dto.OpenAIRole;
import com.basak.dalcom.external_api.openai.service.dto.CompletionAPIRespDto;
import com.basak.dalcom.external_api.openai.service.dto.CompletionAPIRespDto.Choice;
import com.basak.dalcom.external_api.openai.service.dto.CompletionAPIRespDto.Message;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService extends APIServiceImpl {

    private static final String[] PRE_REQUEST_PROMPT = {
        "You are an expert who helps student write better after receiving a script for a speech.",
        "You have to answer in Korean within 100 tokens."
    };
    private final OpenAIConfig openAIConfig;
    private final AIChatLogRepository aiChatLogRepository;

    public OpenAIService(RestTemplate restTemplate, OpenAIConfig openAIConfig,
        AIChatLogRepository aiChatLogRepository) {
        super(restTemplate);
        this.openAIConfig = openAIConfig;
        this.aiChatLogRepository = aiChatLogRepository;
    }

    @Override
    protected URL getUrl(String path) {
        return openAIConfig.getAPIEndpoint();
    }

    @Transactional
    public AIChatLog createPrompt(Speech speech, String prompt) {
        String accountUuid = speech.getPresentation().getUserProfile().getAccount().getUuid();
        Map<String, Object> body = createBody(accountUuid, prompt);

        AIChatLog log = AIChatLog.builder()
            .speech(speech)
            .prompt(prompt)
            .isDone(false)
            .build();

        aiChatLogRepository.save(log);

        try {
            ResponseEntity<Map<String, Object>> response = createResource("", body,
                createHeaders());
            CompletionAPIRespDto dto = new CompletionAPIRespDto(response.getBody());
            if (!dto.getChoices().isEmpty()) {
                Choice choice = dto.getChoices().get(0);
                Message message = choice.getMessage();
                String content = message.getContent();
                log.updateResult(content);
            }

            return log;
        } catch (MalformedURLException ex) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid OpenAI URL");
        }
    }

    public AIChatLogRetrieveResult getAIChatLogsOf(Speech speech) {
        AIChatLogRetrieveResult result = new AIChatLogRetrieveResult(
            aiChatLogRepository.findAllBySpeech(speech));
        return result;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAIConfig.getSecretKey());
        return headers;
    }

    private Map<String, Object> createBody(String accountUuid, String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("user", accountUuid);

        List<Map<String, String>> messages = new ArrayList<>();
        Arrays.stream(PRE_REQUEST_PROMPT)
            .forEach(prompt -> messages.add(createMessage(OpenAIRole.SYSTEM, prompt)));
        messages.add(createMessage(OpenAIRole.USER, content));
        body.put("messages", messages);

        return body;
    }

    private Map<String, String> createMessage(OpenAIRole role, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("role", role.getValue());
        message.put("content", content);
        return message;
    }
}
