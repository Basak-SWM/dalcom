package com.basak.dalcom.domain.core.speech.data;

import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface SpeechRepository extends JpaRepository<Speech, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Speech> findSpeechById(Integer speechId);

    Optional<Speech> findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId);

    List<Speech> findSpeechesByPresentationId(Integer presentationId);

    boolean existsByIdAndPresentationId(Integer speechId, Integer presentationId);

//    @Modifying
//    @Query("UPDATE Speech s SET s.readyToChat = true WHERE s.id = ?1")
//    int updateReadyToChatById(Integer speechId);

//    @Modifying
//    @Query("UPDATE Speech s SET s.readyToChat = :readyToChat WHERE s.id = :id")
//    int updateReadyToChatById(@Param(value = "id") Integer speechId,
//        @Param(value = "readyToChat") boolean readyToChat);
}
