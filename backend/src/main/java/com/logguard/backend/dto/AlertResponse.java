package com.logguard.backend.dto;

import com.logguard.backend.model.AlertStatus;
import com.logguard.backend.model.Severity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AlertResponse {
    private Long id;
    private LocalDateTime createdAt;
    private Severity severity;
    private String message;
    private AlertStatus status;
    private Long anomalyId;
}
