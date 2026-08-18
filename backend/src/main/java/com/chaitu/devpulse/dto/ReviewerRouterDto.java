package com.chaitu.devpulse.dto;

import com.chaitu.devpulse.model.DeveloperNode;

import java.util.List;

public class ReviewerRouterDto {

    public static class ReviewerRecommendation {
        private DeveloperNode developer;
        private double relevanceScore;
        private String matchReason;
        private int reviewCount;

        public ReviewerRecommendation() {}

        public ReviewerRecommendation(DeveloperNode developer, double relevanceScore, String matchReason, int reviewCount) {
            this.developer = developer;
            this.relevanceScore = relevanceScore;
            this.matchReason = matchReason;
            this.reviewCount = reviewCount;
        }

        public DeveloperNode getDeveloper() {
            return developer;
        }

        public void setDeveloper(DeveloperNode developer) {
            this.developer = developer;
        }

        public double getRelevanceScore() {
            return relevanceScore;
        }

        public void setRelevanceScore(double relevanceScore) {
            this.relevanceScore = relevanceScore;
        }

        public String getMatchReason() {
            return matchReason;
        }

        public void setMatchReason(String matchReason) {
            this.matchReason = matchReason;
        }

        public int getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
        }
    }

    private String targetFile;
    private String pullRequestTitle;
    private List<ReviewerRecommendation> recommendedReviewers;
    private String cypherQueryPattern;

    public ReviewerRouterDto() {}

    public ReviewerRouterDto(String targetFile, String pullRequestTitle, List<ReviewerRecommendation> recommendedReviewers, String cypherQueryPattern) {
        this.targetFile = targetFile;
        this.pullRequestTitle = pullRequestTitle;
        this.recommendedReviewers = recommendedReviewers;
        this.cypherQueryPattern = cypherQueryPattern;
    }

    public String getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }

    public String getPullRequestTitle() {
        return pullRequestTitle;
    }

    public void setPullRequestTitle(String pullRequestTitle) {
        this.pullRequestTitle = pullRequestTitle;
    }

    public List<ReviewerRecommendation> getRecommendedReviewers() {
        return recommendedReviewers;
    }

    public void setRecommendedReviewers(List<ReviewerRecommendation> recommendedReviewers) {
        this.recommendedReviewers = recommendedReviewers;
    }

    public String getCypherQueryPattern() {
        return cypherQueryPattern;
    }

    public void setCypherQueryPattern(String cypherQueryPattern) {
        this.cypherQueryPattern = cypherQueryPattern;
    }
}
