package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.ReviewerRouterDto;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;

// Endpoints consolidated in DevPulseController to eliminate duplicate Spring URL mapping conflicts
public class ReviewerController {
    private final DevPulseGraphService devPulseGraphService;

    public ReviewerController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    public ResponseEntity<ReviewerRouterDto> recommendReviewers(String filePath) {
        return ResponseEntity.ok(devPulseGraphService.recommendReviewers(filePath));
    }
}
