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

    public DeveloperService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public List<DeveloperNode> getAllDevelopers() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (d:Developer) RETURN d.id AS id, d.name AS name, d.team AS team, d.tenure AS tenure, d.avatarUrl AS avatarUrl ORDER BY d.name";
                return new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(DeveloperNode.class)
                        .mappedBy((t, r) -> new DeveloperNode(
                                r.get("id").asString(""),
                                r.get("name").asString(""),
                                r.get("team").asString(""),
                                r.get("tenure").asString(""),
                                r.get("avatarUrl").asString("")
                        ))
                        .all());
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error fetching developers: {}", ex.getMessage());
            }
        }
        return Collections.emptyList();
    }
}
