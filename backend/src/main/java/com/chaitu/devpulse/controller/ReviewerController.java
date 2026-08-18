package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.ReviewerRouterDto;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviewers")
public class ReviewerController {

    private final DevPulseGraphService devPulseGraphService;

    public ReviewerController(DevPulseGraphService devPulseGraphService) {
        this.devPulseGraphService = devPulseGraphService;
    }

    @GetMapping("/recommend")
    public ResponseEntity<ReviewerRouterDto> recommendReviewers(@RequestParam(name = "file", defaultValue = "OrderService.java") String filePath) {
        return ResponseEntity.ok(devPulseGraphService.recommendReviewers(filePath));
    }
}
