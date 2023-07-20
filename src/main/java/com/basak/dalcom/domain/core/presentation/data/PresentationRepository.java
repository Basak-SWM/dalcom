package com.basak.dalcom.domain.core.presentation.data;

import com.basak.dalcom.domain.profiles.data.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<Presentation, Integer> {

    List<Presentation> findPresentationsByUserProfile(UserProfile userProfile);

    Optional<Presentation> findPresentationById(Integer presentationId);


}
