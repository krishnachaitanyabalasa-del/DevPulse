package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import org.neo4j.driver.types.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RadarService {

    private static final Logger log = LoggerFactory.getLogger(RadarService.class);
    private final Neo4jClient neo4jClient;
    private final SeedService seedService;

    public RadarService(Neo4jClient neo4jClient, SeedService seedService) {
        this.neo4jClient = neo4jClient;
        this.seedService = seedService;
    }

    public HealthRadarDto getBusFactorRadar() {
        String cypherPattern = "MATCH (target:File) " +
                "OPTIONAL MATCH (dep:File)-[:DEPENDS_ON]->(target) " +
                "OPTIONAL MATCH (pr:PullRequest)-[:CHANGES]->(target) " +
                "OPTIONAL MATCH (rev:Developer)-[:REVIEWED]->(pr) " +
                "WITH target, count(DISTINCT dep) AS inDegree, count(DISTINCT rev) AS revCount, collect(DISTINCT rev) AS reviewers " +
                "RETURN target.id AS f_id, target.path AS f_path, target.extension AS f_ext, target.linesOfCode AS f_loc, " +
                "inDegree, revCount, reviewers " +
                "ORDER BY inDegree DESC";

        List<HealthRadarDto.BusFactorModule> modules = new ArrayList<>();

        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                modules.clear();
                List<HealthRadarDto.BusFactorModule> fetched = new ArrayList<>(neo4jClient.query(cypherPattern)
                        .fetchAs(HealthRadarDto.BusFactorModule.class)
                        .mappedBy((t, r) -> {
                            FileNode file = new FileNode(
                                    r.get("f_id").asString(""),
                                    r.get("f_path").asString(""),
                                    r.get("f_ext").asString(""),
                                    r.get("f_loc").asInt(0)
                            );

                            int inDegree = r.get("inDegree").asInt(0);
                            int revCount = r.get("revCount").asInt(0);
                            boolean isRisk = (inDegree >= 1 && revCount <= 1);

                            DeveloperNode primaryMaintainer = null;
                            List<Object> revList = new ArrayList<>(r.get("reviewers").asList());
                            if (!revList.isEmpty() && revList.get(0) instanceof Node node) {
                                primaryMaintainer = new DeveloperNode(
                                        node.get("id").asString(""),
                                        node.get("name").asString(""),
                                        node.get("team").asString(""),
                                        node.get("tenure").asString(""),
                                        node.get("avatarUrl").asString("")
                                );
                            }

                            return new HealthRadarDto.BusFactorModule(file, inDegree, revCount, isRisk, primaryMaintainer);
                        })
                        .all());

                if (!fetched.isEmpty()) {
                    modules.addAll(fetched);
                    break;
                }
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error executing getBusFactorRadar: {}", ex.getMessage());
            }

            if (attempt == 1) {
                log.info("No files found for Bus Factor Radar. Triggering auto-seed process...");
                seedService.seedDatabase();
            }
        }

        return new HealthRadarDto(modules, modules.size(), cypherPattern);
    }
}
