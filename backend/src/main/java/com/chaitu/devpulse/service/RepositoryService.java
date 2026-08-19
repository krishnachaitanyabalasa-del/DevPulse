package com.chaitu.devpulse.service;

import com.chaitu.devpulse.model.RepositoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryService.class);
    private final Neo4jClient neo4jClient;

    public RepositoryService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public List<RepositoryNode> getAllRepositories() {
        List<RepositoryNode> list = new ArrayList<>();
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                list.clear();
                String cypher = "MATCH (r:Repository) RETURN r.id AS id, r.name AS name, r.language AS language ORDER BY r.name";
                neo4jClient.query(cypher)
                        .fetchAs(Void.class)
                        .mappedBy((t, r) -> {
                            list.add(new RepositoryNode(
                                    r.get("id").asString(""),
                                    r.get("name").asString(""),
                                    r.get("language").asString("")
                            ));
                            return null;
                        })
                        .all();
                break;
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error fetching repositories: {}", ex.getMessage());
            }
        }

        if (list.isEmpty()) {
            list.add(new RepositoryNode("repo_1", "payment-gateway-service", "Java"));
            list.add(new RepositoryNode("repo_2", "auth-identity-service", "Java"));
            list.add(new RepositoryNode("repo_3", "core-api-service", "Java"));
            list.add(new RepositoryNode("repo_4", "infrastructure-config", "HCL"));
            list.add(new RepositoryNode("repo_5", "data-analytics-pipeline", "Python"));
        }

        return list;
    }
}
