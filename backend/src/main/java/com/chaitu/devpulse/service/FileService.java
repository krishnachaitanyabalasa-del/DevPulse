package com.chaitu.devpulse.service;

import com.chaitu.devpulse.model.FileNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final Neo4jClient neo4jClient;
    private final SeedService seedService;

    public FileService(Neo4jClient neo4jClient, SeedService seedService) {
        this.neo4jClient = neo4jClient;
        this.seedService = seedService;
    }

    public List<FileNode> getAllFiles() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (f:File) RETURN f.id AS id, f.path AS path, f.extension AS extension, f.linesOfCode AS linesOfCode ORDER BY f.path";
                List<FileNode> list = new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(FileNode.class)
                        .mappedBy((t, r) -> new FileNode(
                                (r.containsKey("id") && !r.get("id").isNull()) ? r.get("id").asString() : "",
                                (r.containsKey("path") && !r.get("path").isNull()) ? r.get("path").asString() : "",
                                (r.containsKey("extension") && !r.get("extension").isNull()) ? r.get("extension").asString() : "",
                                (r.containsKey("linesOfCode") && !r.get("linesOfCode").isNull()) ? r.get("linesOfCode").asInt() : 0
                        ))
                        .all());
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception ex) {
                log.error("Error fetching files attempt {}: {}", attempt, ex.getMessage(), ex);
            }

            if (attempt == 1) {
                log.info("No files found in graph DB. Auto-seeding graph database...");
                seedService.seedDatabase();
            }
        }
        return Collections.emptyList();
    }
}
