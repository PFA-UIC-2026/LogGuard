package com.logguard.backend.controller;

import com.logguard.backend.dto.LogEntryRequest;
import com.logguard.backend.dto.LogEntryResponse;
import com.logguard.backend.service.LogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogEntryResponse ingest(@Valid @RequestBody LogEntryRequest request) {
        return logService.ingest(request);
    }

    @GetMapping
    public List<LogEntryResponse> getAll() {
        return logService.getAll();
    }
}
