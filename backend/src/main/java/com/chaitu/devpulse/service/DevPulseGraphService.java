package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.dto.HealthStatusDto;
import com.chaitu.devpulse.dto.ReviewerRouterDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import com.chaitu.devpulse.model.PullRequestNode;
import com.chaitu.devpulse.model.RepositoryNode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DevPulseGraphService {

    private final SeedService seedService;
    private final GraphHealthService healthService;
    private final DeveloperService developerService;
    private final FileService fileService;
    private final PullRequestService pullRequestService;
    private final RepositoryService repositoryService;
    private final ExpertFinderService expertFinderService;
    private final ReviewerRouterService reviewerRouterService;
    private final RadarService radarService;

    public DevPulseGraphService(
            SeedService seedService,
            GraphHealthService healthService,
            DeveloperService developerService,
            FileService fileService,
            PullRequestService pullRequestService,
            RepositoryService repositoryService,
            ExpertFinderService expertFinderService,
            ReviewerRouterService reviewerRouterService,
            RadarService radarService
    ) {
        this.seedService = seedService;
        this.healthService = healthService;
        this.developerService = developerService;
        this.fileService = fileService;
        this.pullRequestService = pullRequestService;
        this.repositoryService = repositoryService;
        this.expertFinderService = expertFinderService;
        this.reviewerRouterService = reviewerRouterService;
        this.radarService = radarService;
    }

    public Map<String, Object> seedDatabase() {
        return seedService.seedDatabase();
    }

    public HealthStatusDto checkHealth() {
        return healthService.checkHealth();
    }

    public List<DeveloperNode> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }

    public List<FileNode> getAllFiles() {
        return fileService.getAllFiles();
    }

    public List<PullRequestNode> getAllPullRequests() {
        return pullRequestService.getAllPullRequests();
    }

    public List<RepositoryNode> getAllRepositories() {
        return repositoryService.getAllRepositories();
    }

    public ExpertFinderDto findExperts(String query) {
        return expertFinderService.findExperts(query);
    }

    public ReviewerRouterDto recommendReviewers(String filePath) {
        return reviewerRouterService.recommendReviewers(filePath);
    }

    public HealthRadarDto getBusFactorRadar() {
        return radarService.getBusFactorRadar();
    }
}
