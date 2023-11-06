package com.basak.dalcom.domain.coaching_request.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachingRequestRepository extends JpaRepository<CoachingRequest, Long> {

    List<CoachingRequest> findByUserProfileIdAndCoachProfileId(Integer userProfile_id,
        Integer coachProfile_id);

    List<CoachingRequest> findByUserProfileId(Integer userProfile_id);

    List<CoachingRequest> findByCoachProfileId(Integer coachProfile_id);
}
