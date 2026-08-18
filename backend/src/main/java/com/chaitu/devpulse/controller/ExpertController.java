package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experts")
public class ExpertController {

    private final DevPulseGraphService devPulseGraphService;

    public ExpertController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    @GetMapping
    public ResponseEntity<ExpertFinderDto> findExperts(@RequestParam(name = "query", defaultValue = "OrderService.java") String query) {
        return ResponseEntity.ok(devPulseGraphService.findExperts(query));
    }

    @GetMapping("/developers")
    public ResponseEntity<List<DeveloperNode>> getAllDevelopers() {
        return ResponseEntity.ok(devPulseGraphService.getAllDevelopers());
    }
}
