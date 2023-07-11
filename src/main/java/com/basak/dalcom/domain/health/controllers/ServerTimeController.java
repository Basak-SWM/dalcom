package com.basak.dalcom.domain.health.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/health-check/server-time")
@RestController
public class ServerTimeController {
    @GetMapping("")
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
