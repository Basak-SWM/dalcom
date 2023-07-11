package com.basak.dalcom.domain.core.audio_segment;

import com.basak.dalcom.aws.s3.PresignedURLClient;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AudioUploaderImpl implements AudioSegmentUploader {

    private final PresignedURLClient presignedURLClient;

    @Override
    public String getFilename(Speech speech) {
        Presentation presentation = speech.getPresentation();
        UserProfile userProfile = presentation.getUserProfile();

        StringBuilder builder = new StringBuilder();
        builder.append(userProfile.getId());
        builder.append("/");
        builder.append(presentation.getId());
        builder.append("/");
        builder.append(speech.getId());
        builder.append("/");

        String uuid = UUID.randomUUID().toString();
        builder.append(uuid);

        return builder.toString();
    }

    @Override
    public String getUploadURL(Speech speech, String fileExtension) {
        String filename = getFilename(speech);
        return null;
    }
}
