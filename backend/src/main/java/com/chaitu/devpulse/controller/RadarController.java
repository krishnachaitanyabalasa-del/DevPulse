package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.model.FileNode;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/radar")
public class RadarController {

    private final DevPulseGraphService devPulseGraphService;

    public RadarController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    @GetMapping("/bus-factor")
    public ResponseEntity<HealthRadarDto> getBusFactorRadar() {
        return ResponseEntity.ok(devPulseGraphService.getBusFactorRadar());
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileNode>> getAllFiles() {
        return ResponseEntity.ok(devPulseGraphService.getAllFiles());
    }
}
