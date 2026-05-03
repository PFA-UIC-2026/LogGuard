package com.logguard.backend.service;

import com.logguard.backend.dto.AnomalyResponse;
import com.logguard.backend.repository.AnomalyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnomalyService {

    private final AnomalyRepository anomalyRepository;

    @Transactional(readOnly = true)
    public List<AnomalyResponse> getAll() {
        return anomalyRepository.findAllByOrderByDetectedAtDesc()
                .stream()
                .map(a -> AnomalyResponse.builder()
                        .id(a.getId())
                        .detectedAt(a.getDetectedAt())
                        .type(a.getType())
                        .description(a.getDescription())
                        .severity(a.getSeverity())
                        .triggerLogId(a.getTriggerLog() != null ? a.getTriggerLog().getId() : null)
                        .build())
                .toList();
    }
}
