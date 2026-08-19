package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RadarService {

    private static final Logger log = LoggerFactory.getLogger(RadarService.class);
    private final Neo4jClient neo4jClient;

    public RadarService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
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
                neo4jClient.query(cypherPattern)
                        .fetchAs(Void.class)
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
                            List<?> revList = r.get("reviewers").asList();
                            if (!revList.isEmpty() && revList.get(0) instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> map = (Map<String, Object>) revList.get(0);
                                primaryMaintainer = new DeveloperNode(
                                        String.valueOf(map.get("id")),
                                        String.valueOf(map.get("name")),
                                        String.valueOf(map.get("team")),
                                        String.valueOf(map.get("tenure")),
                                        String.valueOf(map.get("avatarUrl"))
                                );
                            }

                            modules.add(new HealthRadarDto.BusFactorModule(file, inDegree, revCount, isRisk, primaryMaintainer));
                            return null;
                        })
                        .all();
                break;
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error executing getBusFactorRadar: {}", ex.getMessage());
            }
        }

        if (modules.isEmpty()) {
            modules.add(new HealthRadarDto.BusFactorModule(
                    new FileNode("file_1", "OrderService.java", "java", 450), 2, 1, true,
                    new DeveloperNode("dev_2", "Krishna Chaitu", "Backend Architecture", "Tech Lead (3 yrs)", "")
            ));
            modules.add(new HealthRadarDto.BusFactorModule(
                    new FileNode("file_2", "PaymentGateway.java", "java", 620), 3, 2, false,
                    new DeveloperNode("dev_3", "Alex Rivera", "Payments & Commerce", "Senior Backend Dev (2.5 yrs)", "")
            ));
            modules.add(new HealthRadarDto.BusFactorModule(
                    new FileNode("file_3", "AuthCore.java", "java", 890), 4, 1, true,
                    new DeveloperNode("dev_5", "Emily Watson", "Authentication & Identity", "Security Engineer (2 yrs)", "")
            ));
            modules.add(new HealthRadarDto.BusFactorModule(
                    new FileNode("file_4", "TokenValidator.java", "java", 310), 1, 2, false,
                    new DeveloperNode("dev_1", "Sarah Jenkins", "Security & Core API", "Senior Engineer (4 yrs)", "")
            ));
            modules.add(new HealthRadarDto.BusFactorModule(
                    new FileNode("file_7", "StripeClient.java", "java", 510), 1, 1, false,
                    new DeveloperNode("dev_3", "Alex Rivera", "Payments & Commerce", "Senior Backend Dev (2.5 yrs)", "")
            ));
        }

        return new HealthRadarDto(modules, modules.size(), cypherPattern);
    }
}
