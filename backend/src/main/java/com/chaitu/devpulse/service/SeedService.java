package com.chaitu.devpulse.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
    private final Driver driver;

    public SeedService(Neo4jClient neo4jClient, Driver driver) {
        this.neo4jClient = neo4jClient;
        this.driver = driver;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void autoSeedOnStartupIfEmpty() {
        try {
            Long totalNodes = neo4jClient.query("MATCH (n) RETURN count(n) AS count").fetchAs(Long.class).one().orElse(0L);
            if (totalNodes == 0L) {
                log.info("Database is empty on startup (0 nodes). Auto-seeding graph database...");
                seedDatabase();
            } else {
                log.info("Graph database ready with {} existing nodes.", totalNodes);
            }
        } catch (Exception ex) {
            log.warn("Auto-seed check notice: {}", ex.getMessage());
        }
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
            log.info("Parsed {} Cypher statements. Executing in transactional batches...", statements.size());

            // 1. Wipe existing graph in transaction 1
            try (Session session = driver.session()) {
                session.executeWrite(tx -> {
                    tx.run("MATCH (n) DETACH DELETE n");
                    return null;
                });
            }
            log.info("Transaction 1 completed: Wiped existing graph database.");

            // 2. Create nodes and relationships in transaction 2
            try (Session session = driver.session()) {
                session.executeWrite(tx -> {
                    for (String statement : statements) {
                        if (!statement.isBlank() && !statement.toUpperCase().contains("DETACH DELETE")) {
                            tx.run(statement);
                        }
                    }
                    return null;
                });
            }
            log.info("Transaction 2 completed: Created all nodes & relationships.");

            Long totalNodes = 0L;
            Long totalRels = 0L;
            try {
                totalNodes = neo4jClient.query("MATCH (n) RETURN count(n) AS count").fetchAs(Long.class).one().orElse(0L);
                totalRels = neo4jClient.query("MATCH ()-[r]->() RETURN count(r) AS count").fetchAs(Long.class).one().orElse(0L);
            } catch (Exception ignored) {}

            result.put("seeded", true);
            result.put("message", "Successfully seeded production-grade DevPulse graph from seed.cypher file!");
            result.put("statementsExecuted", statements.size());
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
        String[] possiblePaths = {
                "src/main/resources/seed.cypher",
                "seed/seed.cypher",
                "backend/seed/seed.cypher",
                "../seed/seed.cypher"
        };

        for (String pathStr : possiblePaths) {
            File f = new File(pathStr);
            if (f.exists() && f.isFile()) {
                try {
                    String content = Files.readString(f.toPath(), StandardCharsets.UTF_8);
                    log.info("Read seed file from disk: [{}] (Length: {})", f.getAbsolutePath(), content.length());
                    return content;
                } catch (Exception ignored) {}
            }
        }

        try {
            Resource resource = new ClassPathResource("seed.cypher");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    log.info("Read seed file from classpath resource. Length: {}", content.length());
                    return content;
                }
            }
        } catch (Exception ignored) {}

        return null;
    }

    private List<String> parseCypherStatements(String content) {
        List<String> statements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (String line : content.split("\r?\n")) {
            String trimmed = line.trim();
            // Only skip lines that are whole-line comments so URLs like https:// are not truncated!
            if (trimmed.startsWith("//") || trimmed.startsWith("#")) {
                continue;
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
