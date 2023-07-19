package com.basak.dalcom.domain.core.speech.data;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpeechRepository extends JpaRepository<Speech, Integer> {

    Optional<Speech> findSpeechById(Integer speechId);

    @Modifying
    @Transactional
    @Query("update Speech e set e.sttScript=:sttScript where e.id=:speechId")
    void updateSttScriptById(@Param("speechId") Integer speechId, @Param("sttScript") String sttScript);


    Optional<Speech> findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId);

    List<Speech> findSpeechesByPresentationId(Integer presentationId);
}
