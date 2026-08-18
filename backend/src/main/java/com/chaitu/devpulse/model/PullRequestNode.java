package com.chaitu.devpulse.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("PullRequest")
public class PullRequestNode {

    @Id
    private String id;
    private int prNumber;
    private String title;
    private String status;
    private String createdAt;

    public PullRequestNode() {}

    public PullRequestNode(String id, int prNumber, String title, String status, String createdAt) {
        this.id = id;
        this.prNumber = prNumber;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(int prNumber) {
        this.prNumber = prNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
