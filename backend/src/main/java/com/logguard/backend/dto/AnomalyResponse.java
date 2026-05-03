package com.logguard.backend.dto;

import com.logguard.backend.model.AnomalyType;
import com.logguard.backend.model.Severity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnomalyResponse {
    private Long id;
    private LocalDateTime detectedAt;
    private AnomalyType type;
    private String description;
    private Severity severity;
    private Long triggerLogId;
}
