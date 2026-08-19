package com.chaitu.devpulse.service;

import com.chaitu.devpulse.model.RepositoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RepositoryService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryService.class);
    private final Neo4jClient neo4jClient;
    private final SeedService seedService;

    public RepositoryService(Neo4jClient neo4jClient, SeedService seedService) {
        this.neo4jClient = neo4jClient;
        this.seedService = seedService;
    }

    public List<RepositoryNode> getAllRepositories() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (r:Repository) RETURN r.id AS id, r.name AS name, r.language AS language ORDER BY r.name";
                List<RepositoryNode> list = new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(RepositoryNode.class)
                        .mappedBy((t, r) -> new RepositoryNode(
                                (r.containsKey("id") && !r.get("id").isNull()) ? r.get("id").asString() : "",
                                (r.containsKey("name") && !r.get("name").isNull()) ? r.get("name").asString() : "",
                                (r.containsKey("language") && !r.get("language").isNull()) ? r.get("language").asString() : ""
                        ))
                        .all());
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception ex) {
                log.error("Error fetching repositories attempt {}: {}", attempt, ex.getMessage(), ex);
            }

            if (attempt == 1) {
                log.info("No repositories found in graph DB. Auto-seeding graph database...");
                seedService.seedDatabase();
            }
        }
        return Collections.emptyList();
    }
}
