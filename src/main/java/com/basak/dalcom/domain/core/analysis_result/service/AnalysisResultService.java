package com.basak.dalcom.domain.core.analysis_result.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.basak.dalcom.aws.dynamodb.service.DynamoDBService;
import com.basak.dalcom.external_api.redis.repository.RedisAnalysisStatus;
import com.basak.dalcom.external_api.redis.service.RedisService;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnalysisResultService extends DynamoDBService {

    private final RedisService redisService;

    public AnalysisResultService(AmazonDynamoDB dynamoDB,
        String dynamoDBTableName, RedisService redisService) {
        super(dynamoDB, dynamoDBTableName);
        this.redisService = redisService;
    }

    private String getRedisKey(Integer speechId) {
        return String.format("analysis_%d_STATUS", speechId);
    }

    public Optional<RedisAnalysisStatus> getAnalysisStatus(Integer speechId) {
        return redisService.get(getRedisKey(speechId));
    }

    private String getDynamoDBKey(Integer speechId) {
        return String.format("analysis_%d", speechId);
    }

    public Optional<Map<String, Object>> findById(Integer speechId) {
        String key = getDynamoDBKey(speechId);
        return super.findById(key);
    }
}
