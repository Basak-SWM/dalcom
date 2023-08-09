package com.basak.dalcom.external_api.openai.service;

import com.basak.dalcom.config.OpenAIConfig;
import com.basak.dalcom.domain.common.exception.UnhandledException;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
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

    private final OpenAIConfig openAIConfig;
    private final AIChatLogRepository aiChatLogRepository;

    public OpenAIService(RestTemplate restTemplate, OpenAIConfig openAIConfig,
        AIChatLogRepository aiChatLogRepository) {
        super(restTemplate);
        this.openAIConfig = openAIConfig;
        this.aiChatLogRepository = aiChatLogRepository;
    }

    public AIChatLog doFirstSystemPrompt(Speech speech,
        final String[] descriptions, final String textScript) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(descriptions).forEach(sb::append);

        sb.append("This is the script the student wrote : ");
        sb.append(textScript + "\n\n");

        sb.append("The outline of the speech that the student thinks is as follows : ");
        sb.append(speech.getPresentation().getOutline() + "\n\n");

        sb.append("The parts that students want to do well in the speech are as follows : ");
        sb.append(speech.getPresentation().getCheckpoint());

        String prompt = sb.toString();

        AIChatLog emptyAIChatLog = createEmptyAIChatLog(
            speech, prompt, OpenAIRole.SYSTEM
        );

        return doAsyncPrompt(emptyAIChatLog);
    }

    @Transactional
    public Speech doDefaultUserPrompt(AIChatLog systemInitChatLog) {
        Presentation presentation = systemInitChatLog.getSpeech().getPresentation();

        List<String> prompts = new ArrayList<>(2);
        if (presentation.getOutline() != null && !presentation.getOutline().isBlank()) {
            prompts.add("전달된 스크립트는 개요의 내용을 잘 전달하고 있나요? 개요는 다음과 같습니다. : ");
            prompts.add(presentation.getOutline() + "\n\n");
        }
        if (presentation.getCheckpoint() != null && !presentation.getCheckpoint().isBlank()) {
            prompts.add("전달된 스크립트는 다음 요소를 만족하나요? : ");
            prompts.add(presentation.getCheckpoint());
        }

        if (!prompts.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            prompts.forEach(sb::append);
            String prompt = sb.toString();

            Speech speech = systemInitChatLog.getSpeech();
            AIChatLog firstUserPrompt = createEmptyAIChatLog(
                speech, prompt, OpenAIRole.USER
            );
            doAsyncPrompt(firstUserPrompt);
        }

        return systemInitChatLog.getSpeech();
    }

    @Override
    protected URL getUrl(String path) {
        return openAIConfig.getAPIEndpoint();
    }

    @Transactional
    public AIChatLog doAsyncPrompt(AIChatLog log) {
        String accountUuid = log.getSpeech().getPresentation()
            .getUserProfile().getAccount().getUuid();

        Map<String, Object> body = createBody(accountUuid, log.getPrompt());

        try {
            ResponseEntity<Map<String, Object>> response = createResource("", body,
                createHeaders());
            CompletionAPIRespDto dto = new CompletionAPIRespDto(response.getBody());
            if (!dto.getChoices().isEmpty()) {
                Choice choice = dto.getChoices().get(0);
                Message message = choice.getMessage();
                String content = message.getContent();
                log.updateResult(content);
                aiChatLogRepository.save(log);
            }
        } catch (MalformedURLException ex) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid OpenAI URL");
        }

        return log;
    }

    @Transactional
    public AIChatLog createEmptyAIChatLog(Speech speech, String prompt, OpenAIRole role) {
        AIChatLog log = AIChatLog.builder()
            .speech(speech)
            .prompt(prompt)
            .role(role)
            .isDone(false)
            .build();
        aiChatLogRepository.save(log);
        return log;
    }

    public AIChatLogRetrieveResult getAIChatLogsOf(Speech speech) {
        AIChatLogRetrieveResult result = new AIChatLogRetrieveResult(
            aiChatLogRepository.findAllBySpeech(speech));
        return result;
    }

    public AIChatLog getAIChatLogById(Long id) {
        return aiChatLogRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("AIChatLog"));
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
        body.put("temperature", 0.5);
        body.put("max_tokens", 200);

        List<Map<String, String>> messages = new ArrayList<>();
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

    public void deleteAIChatLogs(List<AIChatLog> logs) {
        aiChatLogRepository.deleteAllInBatch(logs);
    }
}
