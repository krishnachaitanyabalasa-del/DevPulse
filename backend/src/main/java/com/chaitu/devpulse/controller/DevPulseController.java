package com.chaitu.devpulse.controller;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.dto.HealthStatusDto;
import com.chaitu.devpulse.dto.ReviewerRouterDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import com.chaitu.devpulse.model.PullRequestNode;
import com.chaitu.devpulse.service.DevPulseGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DevPulseController {

    private final DevPulseGraphService graphService;

    public DevPulseController(DevPulseGraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthStatusDto> getHealth() {
        return ResponseEntity.ok(graphService.checkHealth());
    }

    @RequestMapping(value = "/seed", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> seedDatabase() {
        return ResponseEntity.ok(graphService.seedDatabase());
    }

    @GetMapping("/experts")
    public ResponseEntity<ExpertFinderDto> getExperts(@RequestParam(defaultValue = "OrderService.java") String query) {
        return ResponseEntity.ok(graphService.findExperts(query));
    }

    @GetMapping("/experts/developers")
    public ResponseEntity<List<DeveloperNode>> getAllDevelopers() {
        return ResponseEntity.ok(graphService.getAllDevelopers());
    }

    @GetMapping("/reviewers/recommend")
    public ResponseEntity<ReviewerRouterDto> getRecommendedReviewers(@RequestParam(defaultValue = "OrderService.java") String file) {
        return ResponseEntity.ok(graphService.recommendReviewers(file));
    }

    @GetMapping("/radar/bus-factor")
    public ResponseEntity<HealthRadarDto> getBusFactorRadar() {
        return ResponseEntity.ok(graphService.getBusFactorRadar());
    }

    @GetMapping("/radar/files")
    public ResponseEntity<List<FileNode>> getAllFiles() {
        return ResponseEntity.ok(graphService.getAllFiles());
    }

    @GetMapping("/pull-requests")
    public ResponseEntity<List<PullRequestNode>> getAllPullRequests() {
        return ResponseEntity.ok(graphService.getAllPullRequests());
    }
}
