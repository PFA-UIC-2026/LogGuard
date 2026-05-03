package com.logguard.backend.controller;

import com.logguard.backend.dto.AlertResponse;
import com.logguard.backend.model.AlertStatus;
import com.logguard.backend.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<AlertResponse> getAll(@RequestParam(required = false) AlertStatus status) {
        if (status != null) return alertService.getAlertsByStatus(status);
        return alertService.getAllAlerts();
    }

    @PutMapping("/{id}/resolve")
    public AlertResponse resolve(@PathVariable Long id) {
        return alertService.resolve(id);
    }

    @PutMapping("/{id}/acknowledge")
    public AlertResponse acknowledge(@PathVariable Long id) {
        return alertService.acknowledge(id);
    }
}
