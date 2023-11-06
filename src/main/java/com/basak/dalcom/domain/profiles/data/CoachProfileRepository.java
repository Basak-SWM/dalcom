package com.basak.dalcom.domain.profiles.data;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachProfileRepository extends JpaRepository<CoachProfile, Integer> {

    Optional<CoachProfile> findByAccountUuid(String accountUuid);
}
