package com.basak.dalcom.external_api.redis.repository;

import java.util.Map;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Data
@AllArgsConstructor
@RedisHash(value = "analysis_result")
public class RedisAnalysisStatus {

    @Id
    private String id;

    private Map<String, Status> statusMap;

    public enum Status {
        PENDING, ERROR, SUCCESS
    }
}