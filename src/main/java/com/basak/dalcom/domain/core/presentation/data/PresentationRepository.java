package com.basak.dalcom.domain.core.presentation.data;

import com.basak.dalcom.domain.profiles.data.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<Presentation, Integer> {

    List<Presentation> findByUserProfile(UserProfile userProfile);

    Presentation findPresentationById(Integer presentationId);


}
