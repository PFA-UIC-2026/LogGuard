package com.logguard.backend.service;

import com.logguard.backend.dto.LogEntryRequest;
import com.logguard.backend.dto.LogEntryResponse;
import com.logguard.backend.model.Anomaly;
import com.logguard.backend.model.LogEntry;
import com.logguard.backend.repository.AnomalyRepository;
import com.logguard.backend.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogEntryRepository logEntryRepository;
    private final AnomalyRepository anomalyRepository;
    private final AnomalyDetectionService anomalyDetectionService;
    private final AlertService alertService;

    @Transactional
    public LogEntryResponse ingest(LogEntryRequest request) {
        LogEntry entry = LogEntry.builder()
                .timestamp(LocalDateTime.now())
                .level(request.getLevel())
                .source(request.getSource())
                .message(request.getMessage())
                .rawPayload(request.getRawPayload())
                .build();
        entry = logEntryRepository.save(entry);

        Optional<Anomaly> detected = anomalyDetectionService.analyze(entry);
        if (detected.isPresent()) {
            Anomaly anomaly = anomalyRepository.save(detected.get());
            alertService.createFromAnomaly(anomaly);
        }

        return toResponse(entry);
    }

    @Transactional(readOnly = true)
    public List<LogEntryResponse> getAll() {
        return logEntryRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    private LogEntryResponse toResponse(LogEntry e) {
        return LogEntryResponse.builder()
                .id(e.getId())
                .timestamp(e.getTimestamp())
                .level(e.getLevel())
                .source(e.getSource())
                .message(e.getMessage())
                .build();
    }
}
