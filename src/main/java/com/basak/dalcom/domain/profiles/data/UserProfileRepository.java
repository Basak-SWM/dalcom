package com.basak.dalcom.domain.profiles.data;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    Optional<UserProfile> findByAccountUuid(String accountUuid);
}
