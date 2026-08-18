package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.HealthStatusDto;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final DevPulseGraphService devPulseGraphService;

    public HealthController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    @GetMapping
    public ResponseEntity<HealthStatusDto> getHealth() {
        HealthStatusDto status = devPulseGraphService.checkHealth();
        if (status.isConnected()) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(503).body(status);
        }
    }
}
