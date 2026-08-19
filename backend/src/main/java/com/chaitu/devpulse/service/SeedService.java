package com.chaitu.devpulse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@Service
public class SeedService {

    private static final Logger log = LoggerFactory.getLogger(SeedService.class);
    private final Neo4jClient neo4jClient;

    public SeedService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public Map<String, Object> seedDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Starting production graph database seed process from seed.cypher file...");

            String cypherContent = readSeedCypherFile();
            if (cypherContent == null || cypherContent.isBlank()) {
                result.put("seeded", false);
                result.put("error", "Could not locate or read seed.cypher file.");
                return result;
            }

            List<String> statements = parseCypherStatements(cypherContent);
            int executedCount = 0;

            for (String statement : statements) {
                if (!statement.isBlank()) {
                    try {
                        neo4jClient.query(statement).run();
                        executedCount++;
                    } catch (Exception ex) {
                        log.warn("Cypher statement notice: {}", ex.getMessage());
                    }
                }
            }

            Long totalNodes = 0L;
            Long totalRels = 0L;
            try {
                totalNodes = neo4jClient.query("MATCH (n) RETURN count(n) AS count").fetchAs(Long.class).one().orElse(0L);
                totalRels = neo4jClient.query("MATCH ()-[r]->() RETURN count(r) AS count").fetchAs(Long.class).one().orElse(0L);
            } catch (Exception ignored) {}

            result.put("seeded", true);
            result.put("message", "Successfully seeded production-grade DevPulse graph from seed.cypher file! Executed " + executedCount + " statements.");
            result.put("statementsExecuted", executedCount);
            result.put("nodesCreated", totalNodes);
            result.put("relationshipsCreated", totalRels);

        } catch (Exception ex) {
            log.error("Failed to seed database from file: {}", ex.getMessage(), ex);
            result.put("seeded", false);
            result.put("error", ex.getMessage());
        }
        return result;
    }

    private String readSeedCypherFile() {
        // 1. Try reading from classpath resources
        try {
            Resource resource = new ClassPathResource("seed.cypher");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
        } catch (Exception ignored) {}

        // 2. Try file system relative paths
        String[] possiblePaths = {
                "seed/seed.cypher",
                "backend/seed/seed.cypher",
                "../seed/seed.cypher",
                "src/main/resources/seed.cypher"
        };

        for (String pathStr : possiblePaths) {
            File f = new File(pathStr);
            if (f.exists() && f.isFile()) {
                try {
                    return Files.readString(f.toPath(), StandardCharsets.UTF_8);
                } catch (Exception ignored) {}
            }
        }

        return null;
    }

    private List<String> parseCypherStatements(String content) {
        List<String> statements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (String line : content.split("\r?\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("//") || trimmed.startsWith("#")) {
                continue;
            }
            int commentIdx = line.indexOf("//");
            if (commentIdx >= 0) {
                line = line.substring(0, commentIdx);
            }
            sb.append(line).append("\n");
        }

        for (String rawStmt : sb.toString().split(";")) {
            String stmt = rawStmt.trim();
            if (!stmt.isEmpty()) {
                statements.add(stmt);
            }
        }

        return statements;
    }
}
