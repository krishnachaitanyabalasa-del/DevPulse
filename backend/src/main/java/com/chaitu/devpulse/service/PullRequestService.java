package com.chaitu.devpulse.service;

import com.chaitu.devpulse.model.PullRequestNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PullRequestService {

    private static final Logger log = LoggerFactory.getLogger(PullRequestService.class);
    private final Neo4jClient neo4jClient;

    public PullRequestService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public List<PullRequestNode> getAllPullRequests() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (pr:PullRequest) RETURN pr.id AS id, pr.prNumber AS prNumber, pr.title AS title, pr.status AS status, pr.createdAt AS createdAt ORDER BY pr.prNumber DESC";
                return new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(PullRequestNode.class)
                        .mappedBy((t, r) -> new PullRequestNode(
                                r.get("id").asString(""),
                                r.get("prNumber").asInt(0),
                                r.get("title").asString(""),
                                r.get("status").asString(""),
                                r.get("createdAt").asString("")
                        ))
                        .all());
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error fetching pull requests: {}", ex.getMessage());
            }
        }
        return Collections.emptyList();
    }
}
