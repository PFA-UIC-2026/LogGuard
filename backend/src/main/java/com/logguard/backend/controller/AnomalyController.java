package com.logguard.backend.controller;

import com.logguard.backend.dto.AnomalyResponse;
import com.logguard.backend.service.AnomalyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@RequiredArgsConstructor
public class AnomalyController {

    private final AnomalyService anomalyService;

    @GetMapping
    public List<AnomalyResponse> getAll() {
        return anomalyService.getAll();
    }
}
