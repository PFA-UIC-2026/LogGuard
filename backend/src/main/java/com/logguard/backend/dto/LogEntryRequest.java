package com.logguard.backend.dto;

import com.logguard.backend.model.LogLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogEntryRequest {

    @NotNull(message = "level is required")
    private LogLevel level;

    @NotBlank(message = "source is required")
    private String source;

    @NotBlank(message = "message is required")
    private String message;

    private String rawPayload;
}
