package com.chaitu.devpulse.dto;

import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;

import java.util.List;

public class HealthRadarDto {

    public static class BusFactorModule {
        private FileNode file;
        private int dependencyInDegree;
        private int uniqueReviewersCount;
        private boolean isBusFactorRisk;
        private DeveloperNode primaryMaintainer;

        public BusFactorModule() {}

        public BusFactorModule(FileNode file, int dependencyInDegree, int uniqueReviewersCount, boolean isBusFactorRisk, DeveloperNode primaryMaintainer) {
            this.file = file;
            this.dependencyInDegree = dependencyInDegree;
            this.uniqueReviewersCount = uniqueReviewersCount;
            this.isBusFactorRisk = isBusFactorRisk;
            this.primaryMaintainer = primaryMaintainer;
        }

        public FileNode getFile() {
            return file;
        }

        public void setFile(FileNode file) {
            this.file = file;
        }

        public int getDependencyInDegree() {
            return dependencyInDegree;
        }

        public void setDependencyInDegree(int dependencyInDegree) {
            this.dependencyInDegree = dependencyInDegree;
        }

        public int getUniqueReviewersCount() {
            return uniqueReviewersCount;
        }

        public void setUniqueReviewersCount(int uniqueReviewersCount) {
            this.uniqueReviewersCount = uniqueReviewersCount;
        }

        public boolean isBusFactorRisk() {
            return isBusFactorRisk;
        }

        public void setBusFactorRisk(boolean busFactorRisk) {
            isBusFactorRisk = busFactorRisk;
        }

        public DeveloperNode getPrimaryMaintainer() {
            return primaryMaintainer;
        }

        public void setPrimaryMaintainer(DeveloperNode primaryMaintainer) {
            this.primaryMaintainer = primaryMaintainer;
        }
    }

    private List<BusFactorModule> criticalModules;
    private int totalFilesAnalyzed;
    private String cypherQueryPattern;

    public HealthRadarDto() {}

    public HealthRadarDto(List<BusFactorModule> criticalModules, int totalFilesAnalyzed, String cypherQueryPattern) {
        this.criticalModules = criticalModules;
        this.totalFilesAnalyzed = totalFilesAnalyzed;
        this.cypherQueryPattern = cypherQueryPattern;
    }

    public List<BusFactorModule> getCriticalModules() {
        return criticalModules;
    }

    public void setCriticalModules(List<BusFactorModule> criticalModules) {
        this.criticalModules = criticalModules;
    }

    public int getTotalFilesAnalyzed() {
        return totalFilesAnalyzed;
    }

    public void setTotalFilesAnalyzed(int totalFilesAnalyzed) {
        this.totalFilesAnalyzed = totalFilesAnalyzed;
    }

    public String getCypherQueryPattern() {
        return cypherQueryPattern;
    }

    public void setCypherQueryPattern(String cypherQueryPattern) {
        this.cypherQueryPattern = cypherQueryPattern;
    }
}
