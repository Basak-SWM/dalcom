package com.basak.dalcom.external_api.redis.controller;

import com.basak.dalcom.external_api.redis.repository.RedisAnalysisStatus;
import com.basak.dalcom.external_api.redis.repository.RedisAnalysisStatus.Status;
import com.basak.dalcom.external_api.redis.service.RedisService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/redis-test/{id}")
    public String test(@PathVariable String id) {
        HashMap<String, RedisAnalysisStatus.Status> statusMap = new HashMap<>();
        statusMap.put("task1", Status.SUCCESS);
        statusMap.put("task2", Status.SUCCESS);
        statusMap.put("task3", Status.SUCCESS);

        String createdId = "analysis_" + id + "_STATUS";

        RedisAnalysisStatus dto = RedisAnalysisStatus.builder()
            .id(createdId)
            .statusMap(statusMap)
            .build();

        redisService.save(dto);

        return "test";
    }
}
