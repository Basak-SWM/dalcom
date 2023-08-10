package com.basak.dalcom.domain.core.speech.data;

import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface SpeechRepository extends JpaRepository<Speech, Integer> {

    Optional<Speech> findSpeechById(Integer speechId);

    Optional<Speech> findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId);

    List<Speech> findSpeechesByPresentationId(Integer presentationId);

    boolean existsByIdAndPresentationId(Integer speechId, Integer presentationId);

    @Modifying
    @Query("UPDATE Speech s SET s.readyToChat = ?2 WHERE s.id = ?1")
    int updateReadyToChatById(Integer speechId, boolean readyToChat);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    Optional<Speech> findSpeechByIdAndPresentationIdExclusiveLock(Integer speechId,
        Integer presentationId);
}
