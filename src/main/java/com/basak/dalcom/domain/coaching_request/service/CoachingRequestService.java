package com.basak.dalcom.domain.coaching_request.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.basak.dalcom.aws.s3.S3Service;
import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.data.AccountRole;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.coaching_request.CoachingRequest.Status;
import com.basak.dalcom.domain.coaching_request.data.CoachingRequest;
import com.basak.dalcom.domain.coaching_request.data.CoachingRequestRepository;
import com.basak.dalcom.domain.coaching_request.service.dto.CoachingRequestCreateDto;
import com.basak.dalcom.domain.common.exception.stereotypes.ConflictException;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
import com.basak.dalcom.domain.common.exception.stereotypes.UnauthorizedException;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import com.basak.dalcom.domain.profiles.data.CoachProfile;
import com.basak.dalcom.domain.profiles.data.CoachProfileRepository;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import com.basak.dalcom.domain.profiles.data.UserProfileRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoachingRequestService {

    private final CoachingRequestRepository coachingRequestRepository;
    private final SpeechRepository speechRepository;
    private final CoachProfileRepository coachProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final S3Service s3Service;
    private final PresignedURLService presignedURLService;
    private final AccountService accountService;

    @Transactional
    public CoachingRequest save(CoachingRequestCreateDto dto) throws IOException {
        Speech speech = speechRepository.findById(dto.getSpeechId()).orElseThrow(
            () -> new NotFoundException("Speech"));

        Presentation presentation = speech.getPresentation();

        UserProfile userProfile = userProfileRepository.findByAccountUuid(
                dto.getUserProfileId().toString())
            .orElseThrow(
                () -> new NotFoundException("UserProfile"));

        CoachProfile coachProfile = coachProfileRepository.findByAccountUuid(
                dto.getCoachProfileId().toString())
            .orElseThrow(
                () -> new NotFoundException("CoachProfile"));

        if (!presentation.getUserProfile().getId().equals(userProfile.getId())) {
            throw new UnauthorizedException(
                String.format("스피치 %d에 대한 코칭 요청", dto.getSpeechId()),
                "소유자 권한"
            );
        }

        if (!speech.getRecordDone()) {
            throw new ConflictException("아직 녹음이 완료되지 않은 스피치입니다.");
        }

        List<CoachingRequest> requests = coachingRequestRepository.findByUserProfileIdAndCoachProfileId(
            userProfile.getId(), coachProfile.getId()
        );

        requests.stream()
            .filter(request -> request.getStatus() == Status.REQUESTED)
            .findFirst()
            .ifPresent(request -> {
                throw new ConflictException("이미 코칭 요청을 보낸 코치입니다.");
            });

        String key = speech.getPresentation().getId() + "/" + speech.getId() + "/analysis/STT.json";
        S3Object s3Object = null;
        try {
            s3Object = s3Service.download(key);
        } catch (NotFoundException e) {
            throw new ConflictException("STT가 수행되지 않아 스크립트가 없는 스피치입니다.");
        }

        // Read object content as string
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String stt = new String(bytes);
        inputStream.close();

        CoachingRequest coachingRequest = CoachingRequest.builder()
            .status(Status.REQUESTED)
            .userProfile(userProfile)
            .coachProfile(coachProfile)
            .userMessage(dto.getUserMessage())
            .title(presentation.getTitle())
            .outline(presentation.getOutline())
            .checkpoint(presentation.getCheckpoint())
            .fullAudioUrl(speech.getFullAudioS3Url())
            .sttResult(stt)
            .build();

        coachingRequestRepository.save(coachingRequest);

        return coachingRequest;
    }

    @Transactional(readOnly = true)
    public Optional<CoachingRequest> findById(Long id, Integer accountId)
        throws MalformedURLException {
        Optional<CoachingRequest> found = coachingRequestRepository.findById(id);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        CoachingRequest request = found.get();
        Account account = accountService.findById(accountId).get();
        if (account.getRole() == AccountRole.USER) {
            if (!request.getUserProfile().getAccount().getId().equals(account.getId())) {
                throw new UnauthorizedException("코칭 의뢰 조회", "소유자 권한");
            }
        } else if (account.getRole() == AccountRole.COACH) {
            if (!request.getCoachProfile().getAccount().getId().equals(account.getId())) {
                throw new UnauthorizedException("코칭 의뢰 조회", "소유자 권한");
            }
        }

        URL fullAudioUrl = presignedURLService.signFullURL(new URL(request.getFullAudioUrl()));
        request.setFullAudioUrl(fullAudioUrl.toString());
        return Optional.of(request);
    }

    @Transactional(readOnly = true)
    public List<CoachingRequest> findByAccountId(Integer accountId) {
        Account account = accountService.findById(accountId).get();
        List<CoachingRequest> requests = null;

        if (account.getRole() == AccountRole.USER) {
            requests = coachingRequestRepository.findByUserProfileId(
                account.getUserProfile().getId()
            );
        } else if (account.getRole() == AccountRole.COACH) {
            requests = coachingRequestRepository.findByCoachProfileId(
                account.getCoachProfile().getId()
            );
        }

        requests.forEach(request -> {
            URL fullAudioUrl = null;
            try {
                fullAudioUrl = presignedURLService.signFullURL(
                    new URL(request.getFullAudioUrl()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            request.setFullAudioUrl(fullAudioUrl.toString());
        });

        return requests;
    }

    @Transactional
    public void update(Integer requestedAccountId, Long coachingRequestId,
        String coachMessage, String jsonUserSymbol) {
        Account account = accountService.findById(requestedAccountId).get();
        CoachingRequest coachingRequest = coachingRequestRepository.findById(coachingRequestId)
            .orElseThrow(() -> new NotFoundException("CoachingRequest"));

        if (account.getRole() != AccountRole.COACH ||
            !coachingRequest.getCoachProfile().getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("코칭 의뢰 수정", "담당 코치 권한");
        } else if (!coachingRequest.getStatus().equals(Status.ACCEPTED)) {
            throw new ConflictException("진행 중인 코칭이 아닙니다.");
        }

        coachingRequest.setCoachMessage(coachMessage);
        coachingRequest.setJsonUserSymbol(jsonUserSymbol);
    }

    @Transactional
    public void delete(Integer requestedAccountId, Long coachingRequestId) {
        Account account = accountService.findById(requestedAccountId).get();
        CoachingRequest coachingRequest = coachingRequestRepository.findById(coachingRequestId)
            .orElseThrow(() -> new NotFoundException("CoachingRequest"));

        if (account.getRole() != AccountRole.USER || coachingRequest.getUserProfile().getAccount()
            .getId().equals(account.getId())) {
            throw new UnauthorizedException("코칭 의뢰 삭제", "소유자 권한");
        } else if (coachingRequest.getStatus().equals(Status.ACCEPTED)) {
            throw new UnauthorizedException("코칭 의뢰 삭제", "코칭 완료까지 대기 필요");
        }

        coachingRequestRepository.delete(coachingRequest);
    }

    @Transactional
    public void accept(Integer requestedAccountId, Long coachingRequestId) {
        Account account = accountService.findById(requestedAccountId).get();
        CoachingRequest coachingRequest = coachingRequestRepository.findById(coachingRequestId)
            .orElseThrow(() -> new NotFoundException("CoachingRequest"));

        if (account.getRole() != AccountRole.COACH ||
            !coachingRequest.getCoachProfile().getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("코칭 의뢰 수락", "담당 코치 권한");
        } else if (!coachingRequest.getStatus().equals(Status.REQUESTED)) {
            throw new ConflictException("코칭 요청 상태가 아닙니다.");
        }

        coachingRequest.setStatus(Status.ACCEPTED);
        account.getCoachProfile().setAcceptCount(account.getCoachProfile().getAcceptCount() + 1);
    }

    @Transactional
    public void deny(Integer requestedAccountId, Long coachingRequestId) {
        Account account = accountService.findById(requestedAccountId).get();
        CoachingRequest coachingRequest = coachingRequestRepository.findById(coachingRequestId)
            .orElseThrow(() -> new NotFoundException("CoachingRequest"));

        if (account.getRole() != AccountRole.COACH ||
            !coachingRequest.getCoachProfile().getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("코칭 의뢰 거절", "담당 코치 권한");
        } else if (!coachingRequest.getStatus().equals(Status.REQUESTED)) {
            throw new ConflictException("코칭 요청 상태가 아닙니다.");
        }
        coachingRequest.setDenialReason("Denied");
        coachingRequest.setStatus(Status.DENIED);
    }

    @Transactional
    public void finish(Integer requestAccountId, Long coachingRequestId) {
        Account account = accountService.findById(requestAccountId).get();
        CoachingRequest coachingRequest = coachingRequestRepository.findById(coachingRequestId)
            .orElseThrow(() -> new NotFoundException("CoachingRequest"));

        if (account.getRole() != AccountRole.USER ||
            !coachingRequest.getUserProfile().getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("코칭 의뢰 수정", "담당 유저 권한");
        } else if (!coachingRequest.getStatus().equals(Status.ACCEPTED)) {
            throw new ConflictException("진행 중인 코칭이 아닙니다.");
        }

        coachingRequest.setStatus(Status.DONE);
    }
}
