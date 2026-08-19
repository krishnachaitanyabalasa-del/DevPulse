package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.HealthStatusDto;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;

// Endpoints consolidated in DevPulseController to eliminate duplicate Spring URL mapping conflicts
public class HealthController {
    private final DevPulseGraphService devPulseGraphService;

    public HealthController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    public ResponseEntity<HealthStatusDto> getHealth() {
        HealthStatusDto status = devPulseGraphService.checkHealth();
        return ResponseEntity.ok(status);
    }
}
