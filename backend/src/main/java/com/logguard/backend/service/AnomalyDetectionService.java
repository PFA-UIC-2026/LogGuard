package com.logguard.backend.service;

import com.logguard.backend.model.*;
import com.logguard.backend.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnomalyDetectionService {

    private final LogEntryRepository logEntryRepository;

    private static final Set<String> CRITICAL_KEYWORDS = Set.of(
            "OutOfMemoryError", "OOM", "StackOverflowError"
    );
    private static final Set<String> HIGH_KEYWORDS = Set.of(
            "NullPointerException", "Connection refused", "timed out", "timeout",
            "FATAL", "deadlock", "disk full"
    );

    private static final int ERROR_RATE_THRESHOLD = 10;
    private static final int ERROR_RATE_WINDOW_MINUTES = 5;
    private static final int REPEATED_MSG_THRESHOLD = 5;
    private static final int REPEATED_MSG_WINDOW_MINUTES = 5;

    public Optional<Anomaly> analyze(LogEntry log) {
        Optional<Anomaly> keyword = checkKeywordMatch(log);
        if (keyword.isPresent()) return keyword;

        Optional<Anomaly> rateSpike = checkErrorRateSpike(log);
        if (rateSpike.isPresent()) return rateSpike;

        return checkRepeatedMessage(log);
    }

    private Optional<Anomaly> checkKeywordMatch(LogEntry log) {
        String msg = log.getMessage();
        for (String kw : CRITICAL_KEYWORDS) {
            if (msg.contains(kw)) {
                return Optional.of(build(AnomalyType.KEYWORD_MATCH, Severity.CRITICAL,
                        "Critical keyword detected: " + kw, log));
            }
        }
        for (String kw : HIGH_KEYWORDS) {
            if (msg.toLowerCase().contains(kw.toLowerCase())) {
                return Optional.of(build(AnomalyType.KEYWORD_MATCH, Severity.HIGH,
                        "High-severity keyword detected: " + kw, log));
            }
        }
        return Optional.empty();
    }

    private Optional<Anomaly> checkErrorRateSpike(LogEntry log) {
        if (log.getLevel() != LogLevel.ERROR && log.getLevel() != LogLevel.FATAL) {
            return Optional.empty();
        }
        LocalDateTime windowStart = log.getTimestamp().minusMinutes(ERROR_RATE_WINDOW_MINUTES);
        List<LogEntry> recent = logEntryRepository.findBySourceAndLevelInAndTimestampAfter(
                log.getSource(), List.of(LogLevel.ERROR, LogLevel.FATAL), windowStart);

        if (recent.size() >= ERROR_RATE_THRESHOLD) {
            return Optional.of(build(AnomalyType.ERROR_RATE_SPIKE, Severity.HIGH,
                    String.format("Error rate spike: %d errors from '%s' in the last %d minutes",
                            recent.size(), log.getSource(), ERROR_RATE_WINDOW_MINUTES),
                    log));
        }
        return Optional.empty();
    }

    private Optional<Anomaly> checkRepeatedMessage(LogEntry log) {
        LocalDateTime windowStart = log.getTimestamp().minusMinutes(REPEATED_MSG_WINDOW_MINUTES);
        List<LogEntry> repeated = logEntryRepository.findByMessageAndTimestampAfter(
                log.getMessage(), windowStart);

        if (repeated.size() >= REPEATED_MSG_THRESHOLD) {
            return Optional.of(build(AnomalyType.REPEATED_MESSAGE, Severity.MEDIUM,
                    String.format("Repeated message %d times in %d minutes: %s",
                            repeated.size(), REPEATED_MSG_WINDOW_MINUTES, truncate(log.getMessage(), 100)),
                    log));
        }
        return Optional.empty();
    }

    private Anomaly build(AnomalyType type, Severity severity, String description, LogEntry trigger) {
        return Anomaly.builder()
                .detectedAt(LocalDateTime.now())
                .type(type)
                .severity(severity)
                .description(description)
                .triggerLog(trigger)
                .build();
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
