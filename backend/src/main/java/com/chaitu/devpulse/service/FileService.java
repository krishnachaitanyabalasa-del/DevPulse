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

    public FileService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public List<FileNode> getAllFiles() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                String cypher = "MATCH (f:File) RETURN f.id AS id, f.path AS path, f.extension AS extension, f.linesOfCode AS linesOfCode ORDER BY f.path";
                return new ArrayList<>(neo4jClient.query(cypher)
                        .fetchAs(FileNode.class)
                        .mappedBy((t, r) -> new FileNode(
                                r.get("id").asString(""),
                                r.get("path").asString(""),
                                r.get("extension").asString(""),
                                r.get("linesOfCode").asInt(0)
                        ))
                        .all());
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error fetching files: {}", ex.getMessage());
            }
        }
        return Collections.emptyList();
    }
}
