package com.basak.dalcom.domain.core.presentation.data;

import com.basak.dalcom.domain.profiles.data.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<Presentation, Integer> {

    Slice<Presentation> findSliceByUserProfile(UserProfile userProfile, Pageable pageable);


}
