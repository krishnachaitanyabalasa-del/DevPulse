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
    private final SeedService seedService;

    public PullRequestService(Neo4jClient neo4jClient, SeedService seedService) {
        this.neo4jClient = neo4jClient;
        this.seedService = seedService;
    }

    public List<PullRequestNode> getAllPullRequests() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (p:PullRequest) RETURN p.id AS id, p.prNumber AS prNumber, p.title AS title, p.status AS status, p.createdAt AS createdAt ORDER BY p.prNumber DESC";
                List<PullRequestNode> list = new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(PullRequestNode.class)
                        .mappedBy((t, r) -> new PullRequestNode(
                                (r.containsKey("id") && !r.get("id").isNull()) ? r.get("id").asString() : "",
                                (r.containsKey("prNumber") && !r.get("prNumber").isNull()) ? r.get("prNumber").asInt() : 0,
                                (r.containsKey("title") && !r.get("title").isNull()) ? r.get("title").asString() : "",
                                (r.containsKey("status") && !r.get("status").isNull()) ? r.get("status").asString() : "",
                                (r.containsKey("createdAt") && !r.get("createdAt").isNull()) ? r.get("createdAt").asString() : ""
                        ))
                        .all());
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception ex) {
                log.error("Error fetching pull requests attempt {}: {}", attempt, ex.getMessage(), ex);
            }

            if (attempt == 1) {
                log.info("No pull requests found in graph DB. Auto-seeding graph database...");
                seedService.seedDatabase();
            }
        }
        return Collections.emptyList();
    }
}
