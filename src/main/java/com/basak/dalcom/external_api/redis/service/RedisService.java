package com.basak.dalcom.external_api.redis.service;

import com.basak.dalcom.external_api.redis.repository.AnalysisResultRedisRepository;
import com.basak.dalcom.external_api.redis.repository.RedisAnalysisStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final AnalysisResultRedisRepository repository;

    public RedisAnalysisStatus save(RedisAnalysisStatus dto) {
        repository.save(dto);
        return dto;
    }

    public Optional<RedisAnalysisStatus> get(String id) {
        return repository.findById(id);
    }
}
