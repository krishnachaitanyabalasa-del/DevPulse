package com.chaitu.devpulse.service;

import com.chaitu.devpulse.model.DeveloperNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeveloperService {

    private static final Logger log = LoggerFactory.getLogger(DeveloperService.class);
    private final Neo4jClient neo4jClient;
    private final SeedService seedService;

    public DeveloperService(Neo4jClient neo4jClient, SeedService seedService) {
        this.neo4jClient = neo4jClient;
        this.seedService = seedService;
    }

    public List<DeveloperNode> getAllDevelopers() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (d:Developer) RETURN d.id AS id, d.name AS name, d.team AS team, d.tenure AS tenure, d.avatarUrl AS avatarUrl ORDER BY d.name";
                List<DeveloperNode> list = new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(DeveloperNode.class)
                        .mappedBy((t, r) -> new DeveloperNode(
                                (r.containsKey("id") && !r.get("id").isNull()) ? r.get("id").asString() : "",
                                (r.containsKey("name") && !r.get("name").isNull()) ? r.get("name").asString() : "",
                                (r.containsKey("team") && !r.get("team").isNull()) ? r.get("team").asString() : "",
                                (r.containsKey("tenure") && !r.get("tenure").isNull()) ? r.get("tenure").asString() : "",
                                (r.containsKey("avatarUrl") && !r.get("avatarUrl").isNull()) ? r.get("avatarUrl").asString() : ""
                        ))
                        .all());
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception ex) {
                log.error("Error fetching developers attempt {}: {}", attempt, ex.getMessage(), ex);
            }

            if (attempt == 1) {
                log.info("No developers found in graph DB. Auto-seeding graph database...");
                seedService.seedDatabase();
            }
        }
        return Collections.emptyList();
    }
}
