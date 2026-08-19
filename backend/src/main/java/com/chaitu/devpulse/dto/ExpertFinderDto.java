package com.chaitu.devpulse.dto;

import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;

import java.util.List;

public class ExpertFinderDto {

    public static class ExpertDetail {
        private DeveloperNode developer;
        private int reviewCount;
        private double averageReviewScore;
        private int proximityHops;
        private List<String> pathChain;

        public ExpertDetail() {}

        public ExpertDetail(DeveloperNode developer, int reviewCount, double averageReviewScore, int proximityHops, List<String> pathChain) {
            this.developer = developer;
            this.reviewCount = reviewCount;
            this.averageReviewScore = averageReviewScore;
            this.proximityHops = proximityHops;
            this.pathChain = pathChain;
        }

        public DeveloperNode getDeveloper() {
            return developer;
        }

        public void setDeveloper(DeveloperNode developer) {
            this.developer = developer;
        }

        public int getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
        }

        public double getAverageReviewScore() {
            return averageReviewScore;
        }

        public void setAverageReviewScore(double averageReviewScore) {
            this.averageReviewScore = averageReviewScore;
        }

        public int getProximityHops() {
            return proximityHops;
        }

        public void setProximityHops(int proximityHops) {
            this.proximityHops = proximityHops;
        }

        public List<String> getPathChain() {
            return pathChain;
        }

        public void setPathChain(List<String> pathChain) {
            this.pathChain = pathChain;
        }
    }

    private String searchQuery;
    private FileNode targetFile;
    private List<ExpertDetail> experts;
    private int totalExperts;
    private String cypherQueryPattern;
    private boolean found;
    private String message;

    public ExpertFinderDto() {}

    public ExpertFinderDto(String searchQuery, FileNode targetFile, List<ExpertDetail> experts, int totalExperts, String cypherQueryPattern, boolean found, String message) {
        this.searchQuery = searchQuery;
        this.targetFile = targetFile;
        this.experts = experts;
        this.totalExperts = totalExperts;
        this.cypherQueryPattern = cypherQueryPattern;
        this.found = found;
        this.message = message;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public FileNode getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(FileNode targetFile) {
        this.targetFile = targetFile;
    }

    public List<ExpertDetail> getExperts() {
        return experts;
    }

    public void setExperts(List<ExpertDetail> experts) {
        this.experts = experts;
    }

    public int getTotalExperts() {
        return totalExperts;
    }

    public void setTotalExperts(int totalExperts) {
        this.totalExperts = totalExperts;
    }

    public String getCypherQueryPattern() {
        return cypherQueryPattern;
    }

    public void setCypherQueryPattern(String cypherQueryPattern) {
        this.cypherQueryPattern = cypherQueryPattern;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
