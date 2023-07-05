package com.basak.dalcom.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDatetimeAudit extends CreatedAtAudit {
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
