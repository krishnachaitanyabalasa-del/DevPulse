package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpertFinderService {

    private static final Logger log = LoggerFactory.getLogger(ExpertFinderService.class);
    private final Neo4jClient neo4jClient;

    public ExpertFinderService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public ExpertFinderDto findExperts(String query) {
        if (query == null || query.isBlank()) {
            return new ExpertFinderDto("", null, Collections.emptyList(), 0, "", false, "Please enter a file path or keyword to search for experts.");
        }

        String cleanQuery = query.trim();

        String fileCheckCypher = "MATCH (f:File) WHERE toLower(f.path) CONTAINS toLower($q) OR toLower(f.id) CONTAINS toLower($q) RETURN f.id AS f_id, f.path AS f_path, f.extension AS f_ext, f.linesOfCode AS f_loc LIMIT 1";
        FileNode foundFile = null;
        try {
            foundFile = neo4jClient.query(fileCheckCypher)
                    .bind(cleanQuery).to("q")
                    .fetchAs(FileNode.class)
                    .mappedBy((t, r) -> new FileNode(
                            r.get("f_id").asString(""),
                            r.get("f_path").asString(""),
                            r.get("f_ext").asString(""),
                            r.get("f_loc").asInt(0)
                    ))
                    .one()
                    .orElse(null);
        } catch (Exception ex) {
            log.error("File check error: {}", ex.getMessage());
        }

        if (foundFile == null) {
            return new ExpertFinderDto(
                    cleanQuery,
                    null,
                    Collections.emptyList(),
                    0,
                    fileCheckCypher,
                    false,
                    "The searched file '" + cleanQuery + "' is not in the project codebase."
            );
        }

        String cypherPattern = "MATCH (f:File) WHERE toLower(f.path) CONTAINS toLower($q) OR toLower(f.id) CONTAINS toLower($q) " +
                "MATCH (pr:PullRequest)-[:CHANGES]->(f) " +
                "MATCH (dev:Developer)-[r:REVIEWED]->(pr) " +
                "RETURN f.id AS f_id, f.path AS f_path, f.extension AS f_ext, f.linesOfCode AS f_loc, " +
                "dev.id AS d_id, dev.name AS d_name, dev.team AS d_team, dev.tenure AS d_tenure, dev.avatarUrl AS d_avatar, " +
                "r.score AS score, pr.title AS pr_title, pr.prNumber AS pr_num";

        final FileNode targetFile = foundFile;
        Map<String, ExpertFinderDto.ExpertDetail> expertMap = new HashMap<>();

        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                expertMap.clear();

                neo4jClient.query(cypherPattern)
                        .bind(cleanQuery).to("q")
                        .fetchAs(Void.class)
                        .mappedBy((t, r) -> {
                            String devId = r.get("d_id").asString("");
                            DeveloperNode dev = new DeveloperNode(
                                    devId,
                                    r.get("d_name").asString(""),
                                    r.get("d_team").asString(""),
                                    r.get("d_tenure").asString(""),
                                    r.get("d_avatar").asString("")
                            );

                            double score = r.get("score").asDouble(90.0);
                            String prTitle = r.get("pr_title").asString("");
                            int prNum = r.get("pr_num").asInt(0);

                            List<String> pathChain = Arrays.asList(
                                    targetFile.getPath(),
                                    "PR #" + prNum + " (" + prTitle + ")",
                                    dev.getName() + " (" + dev.getTeam() + ")"
                            );

                            if (!expertMap.containsKey(devId)) {
                                expertMap.put(devId, new ExpertFinderDto.ExpertDetail(dev, 1, score, 2, pathChain));
                            } else {
                                ExpertFinderDto.ExpertDetail existing = expertMap.get(devId);
                                existing.setReviewCount(existing.getReviewCount() + 1);
                                existing.setAverageReviewScore((existing.getAverageReviewScore() + score) / 2.0);
                            }
                            return null;
                        })
                        .all();
                break;
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error executing findExperts: {}", ex.getMessage());
            }
        }

        List<ExpertFinderDto.ExpertDetail> expertsList = new ArrayList<>(expertMap.values());
        expertsList.sort((a, b) -> Integer.compare(b.getReviewCount(), a.getReviewCount()));

        return new ExpertFinderDto(
                cleanQuery,
                targetFile,
                expertsList,
                expertsList.size(),
                cypherPattern,
                true,
                "Found " + expertsList.size() + " expert developers for file " + targetFile.getPath()
        );
    }
}
