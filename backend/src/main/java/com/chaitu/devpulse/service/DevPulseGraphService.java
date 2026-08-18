package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.dto.HealthStatusDto;
import com.chaitu.devpulse.dto.ReviewerRouterDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import com.chaitu.devpulse.model.PullRequestNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DevPulseGraphService {

    private final Neo4jClient neo4jClient;

    @Value("${spring.neo4j.uri:bolt+s://databases.cognodb.com}")
    private String databaseUri;

    public DevPulseGraphService(Neo4jClient neo4jClient) {
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
            return new HealthStatusDto(
                    false,
                    databaseUri,
                    0,
                    0,
                    "Database unreachable: " + ex.getMessage()
            );
        }
    }

    public List<DeveloperNode> getAllDevelopers() {
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
    }

    public List<FileNode> getAllFiles() {
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
    }

    public List<PullRequestNode> getAllPullRequests() {
        String cypher = "MATCH (pr:PullRequest) RETURN pr.id AS id, pr.prNumber AS prNumber, pr.title AS title, pr.status AS status, pr.createdAt AS createdAt ORDER BY pr.prNumber DESC";
        return new ArrayList<>(neo4jClient.query(cypher)
                .fetchAs(PullRequestNode.class)
                .mappedBy((t, r) -> new PullRequestNode(
                        r.get("id").asString(""),
                        r.get("prNumber").asInt(0),
                        r.get("title").asString(""),
                        r.get("status").asString(""),
                        r.get("createdAt").asString("")
                ))
                .all());
    }

    public ExpertFinderDto findExperts(String query) {
        String cleanQuery = (query == null || query.isBlank()) ? "OrderService.java" : query.trim();

        String cypherPattern = "MATCH (f:File) WHERE toLower(f.path) CONTAINS toLower($q) OR toLower(f.id) = toLower($q) " +
                "MATCH (pr:PullRequest)-[:CHANGES]->(f) " +
                "MATCH (dev:Developer)-[r:REVIEWED]->(pr) " +
                "RETURN f.id AS f_id, f.path AS f_path, f.extension AS f_ext, f.linesOfCode AS f_loc, " +
                "dev.id AS d_id, dev.name AS d_name, dev.team AS d_team, dev.tenure AS d_tenure, dev.avatarUrl AS d_avatar, " +
                "r.score AS score, pr.title AS pr_title, pr.prNumber AS pr_num";

        final FileNode[] fileHolder = new FileNode[1];
        Map<String, ExpertFinderDto.ExpertDetail> expertMap = new HashMap<>();

        neo4jClient.query(cypherPattern)
                .bind(cleanQuery).to("q")
                .fetchAs(Void.class)
                .mappedBy((t, r) -> {
                    if (fileHolder[0] == null) {
                        fileHolder[0] = new FileNode(
                                r.get("f_id").asString(""),
                                r.get("f_path").asString(""),
                                r.get("f_ext").asString(""),
                                r.get("f_loc").asInt(0)
                        );
                    }

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
                            fileHolder[0].getPath(),
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

        List<ExpertFinderDto.ExpertDetail> expertsList = new ArrayList<>(expertMap.values());
        expertsList.sort((a, b) -> Integer.compare(b.getReviewCount(), a.getReviewCount()));

        return new ExpertFinderDto(
                cleanQuery,
                fileHolder[0],
                expertsList,
                expertsList.size(),
                cypherPattern
        );
    }

    public ReviewerRouterDto recommendReviewers(String filePath) {
        String cleanPath = (filePath == null || filePath.isBlank()) ? "OrderService.java" : filePath.trim();

        String cypherPattern = "MATCH (f:File) WHERE toLower(f.path) = toLower($file) OR toLower(f.id) = toLower($file) " +
                "MATCH (pr:PullRequest)-[:CHANGES]->(f) " +
                "MATCH (dev:Developer)-[r:REVIEWED]->(pr) " +
                "RETURN dev.id AS d_id, dev.name AS d_name, dev.team AS d_team, dev.tenure AS d_tenure, dev.avatarUrl AS d_avatar, " +
                "count(pr) AS reviewCount, avg(r.score) AS avgScore " +
                "ORDER BY reviewCount DESC, avgScore DESC LIMIT 3";

        List<ReviewerRouterDto.ReviewerRecommendation> list = new ArrayList<>();

        neo4jClient.query(cypherPattern)
                .bind(cleanPath).to("file")
                .fetchAs(Void.class)
                .mappedBy((t, r) -> {
                    DeveloperNode dev = new DeveloperNode(
                            r.get("d_id").asString(""),
                            r.get("d_name").asString(""),
                            r.get("d_team").asString(""),
                            r.get("d_tenure").asString(""),
                            r.get("d_avatar").asString("")
                    );

                    int count = r.get("reviewCount").asInt(1);
                    double score = r.get("avgScore").asDouble(90.0);
                    String reason = "Reviewed " + count + " PRs touching this file or its direct dependency hierarchy.";

                    list.add(new ReviewerRouterDto.ReviewerRecommendation(dev, score, reason, count));
                    return null;
                })
                .all();

        return new ReviewerRouterDto(cleanPath, "PR Review Recommendation for " + cleanPath, list, cypherPattern);
    }

    public HealthRadarDto getBusFactorRadar() {
        String cypherPattern = "MATCH (dep:File)-[:DEPENDS_ON]->(target:File) " +
                "OPTIONAL MATCH (pr:PullRequest)-[:CHANGES]->(target) " +
                "OPTIONAL MATCH (rev:Developer)-[:REVIEWED]->(pr) " +
                "WITH target, count(DISTINCT dep) AS inDegree, count(DISTINCT rev) AS revCount, collect(DISTINCT rev) AS reviewers " +
                "RETURN target.id AS f_id, target.path AS f_path, target.extension AS f_ext, target.linesOfCode AS f_loc, " +
                "inDegree, revCount, reviewers " +
                "ORDER BY inDegree DESC";

        List<HealthRadarDto.BusFactorModule> modules = new ArrayList<>();

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
                    boolean isRisk = (inDegree >= 1 && revCount <= 1); // High dependency, single reviewer!

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

        return new HealthRadarDto(modules, modules.size(), cypherPattern);
    }
}
