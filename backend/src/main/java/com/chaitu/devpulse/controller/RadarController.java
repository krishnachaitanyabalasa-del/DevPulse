package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.model.FileNode;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;

import java.util.List;

// Endpoints consolidated in DevPulseController to eliminate duplicate Spring URL mapping conflicts
public class RadarController {
    private final DevPulseGraphService devPulseGraphService;

    public RadarController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    public ResponseEntity<HealthRadarDto> getBusFactorRadar() {
        return ResponseEntity.ok(devPulseGraphService.getBusFactorRadar());
    }

    public ResponseEntity<List<FileNode>> getAllFiles() {
        return ResponseEntity.ok(devPulseGraphService.getAllFiles());
    }
}
