package com.logguard.backend.service;

import com.logguard.backend.dto.AlertResponse;
import com.logguard.backend.exception.ResourceNotFoundException;
import com.logguard.backend.model.Alert;
import com.logguard.backend.model.AlertStatus;
import com.logguard.backend.model.Anomaly;
import com.logguard.backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public Alert createFromAnomaly(Anomaly anomaly) {
        Alert alert = Alert.builder()
                .createdAt(LocalDateTime.now())
                .severity(anomaly.getSeverity())
                .message("[" + anomaly.getType() + "] " + anomaly.getDescription())
                .status(AlertStatus.OPEN)
                .anomaly(anomaly)
                .build();
        return alertRepository.save(alert);
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public AlertResponse resolve(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
        alert.setStatus(AlertStatus.RESOLVED);
        return toResponse(alertRepository.save(alert));
    }

    @Transactional
    public AlertResponse acknowledge(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
        alert.setStatus(AlertStatus.ACKNOWLEDGED);
        return toResponse(alertRepository.save(alert));
    }

    private AlertResponse toResponse(Alert a) {
        return AlertResponse.builder()
                .id(a.getId())
                .createdAt(a.getCreatedAt())
                .severity(a.getSeverity())
                .message(a.getMessage())
                .status(a.getStatus())
                .anomalyId(a.getAnomaly() != null ? a.getAnomaly().getId() : null)
                .build();
    }
}
