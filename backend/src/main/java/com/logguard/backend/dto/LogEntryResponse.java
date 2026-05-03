package com.logguard.backend.dto;

import com.logguard.backend.model.LogLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LogEntryResponse {
    private Long id;
    private LocalDateTime timestamp;
    private LogLevel level;
    private String source;
    private String message;
}
