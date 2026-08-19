package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.HealthStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
public class GraphHealthService {

    private static final Logger log = LoggerFactory.getLogger(GraphHealthService.class);
    private final Neo4jClient neo4jClient;

    @Value("${spring.neo4j.uri:bolt+s://db-7ba26f4a.databases.cognodb.com:7687}")
    private String databaseUri;

    public GraphHealthService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public HealthStatusDto checkHealth() {
        try {
            Long nodeCount = neo4jClient.query("MATCH (n) RETURN count(n) AS count")
                    .fetchAs(Long.class)
                    .one()
                    .orElse(0L);

            Long relCount = neo4jClient.query("MATCH ()-[r]->() RETURN count(r) AS count")
                    .fetchAs(Long.class)
                    .one()
                    .orElse(0L);

            return new HealthStatusDto(
                    true,
                    databaseUri,
                    nodeCount,
                    relCount,
                    "Connected to CognoDB DevPulse Graph Database."
            );
        } catch (Exception ex) {
            log.warn("CognoDB Health Check Warning: {}", ex.getMessage());
            return new HealthStatusDto(
                    false,
                    databaseUri,
                    0,
                    0,
                    "Database connection error: " + ex.getMessage()
            );
        }
    }
}
