package com.basak.dalcom.external_api.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisResultRedisRepository extends CrudRepository<RedisAnalysisStatus, String> {

}
