package com.chaitu.devpulse.dto;

public class HealthStatusDto {
    private boolean connected;
    private String databaseUri;
    private long nodeCount;
    private long relationshipCount;
    private String message;

    public HealthStatusDto() {}

    public HealthStatusDto(boolean connected, String databaseUri, long nodeCount, long relationshipCount, String message) {
        this.connected = connected;
        this.databaseUri = databaseUri;
        this.nodeCount = nodeCount;
        this.relationshipCount = relationshipCount;
        this.message = message;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getDatabaseUri() {
        return databaseUri;
    }

    public void setDatabaseUri(String databaseUri) {
        this.databaseUri = databaseUri;
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(long nodeCount) {
        this.nodeCount = nodeCount;
    }

    public long getRelationshipCount() {
        return relationshipCount;
    }

    public void setRelationshipCount(long relationshipCount) {
        this.relationshipCount = relationshipCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
