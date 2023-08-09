package com.basak.dalcom.domain.core.speech.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.external_api.openai.controller.dto.OpenAIRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "ai_chat_log")
public class AIChatLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "speech_id", nullable = false)
    private Speech speech;

    @Column(columnDefinition = "TEXT", length = 1000, nullable = false)
    private String prompt;

    @Column(columnDefinition = "TEXT", length = 1000)
    private String result;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean isDone;

    @Column(nullable = false)
    private OpenAIRole role;

    public void updateResult(String result) {
        this.result = result;
        this.isDone = true;
    }

    public void disconnectWithSpeech() {
        this.speech = null;
    }
}
