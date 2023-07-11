package com.basak.dalcom.domain.core.audio_segment;

import com.basak.dalcom.domain.core.speech.data.Speech;

public interface AudioSegmentUploader {

    String getFilename(Speech speech);

    String getUploadURL(Speech speech, String fileExtension);
}
