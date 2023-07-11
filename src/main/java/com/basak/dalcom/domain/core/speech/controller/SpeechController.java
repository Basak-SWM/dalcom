package com.basak.dalcom.domain.core.speech.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/presentations/{presentationId}/speeches")
public class SpeechController {

//    private final SpeechService speechService;
//    private final PresignedURLClient presignedURLClient;
//
//    @PostMapping("/{speechId}/upload-url")
//    public String createPresignedURL(@PathVariable("presentationId") Integer presentationId,
//        @PathVariable("speechId") Integer speechId) {
//        Speech speechOptional = speechService.findSpeechById(presentationId, speechId)
//            .orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Speech not found"));
////        presignedURLClient.getPreSignedUrl("yubin-local-tokpeanut", )
//        return presignedURLClient.getPreSignedUrl("yubin-local-tokpeanut", "");
//    }
}
