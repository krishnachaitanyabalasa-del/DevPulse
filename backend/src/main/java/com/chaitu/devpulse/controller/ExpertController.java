package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;

import java.util.List;

// Endpoints consolidated in DevPulseController to eliminate duplicate Spring URL mapping conflicts
public class ExpertController {
    private final DevPulseGraphService devPulseGraphService;

    public ExpertController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    public ResponseEntity<ExpertFinderDto> findExperts(String query) {
        return ResponseEntity.ok(devPulseGraphService.findExperts(query));
    }

    public ResponseEntity<List<DeveloperNode>> getAllDevelopers() {
        return ResponseEntity.ok(devPulseGraphService.getAllDevelopers());
    }
}
