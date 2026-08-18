package com.chaitu.devpulse.service;

import com.chaitu.devpulse.dto.ExpertFinderDto;
import com.chaitu.devpulse.dto.HealthRadarDto;
import com.chaitu.devpulse.dto.HealthStatusDto;
import com.chaitu.devpulse.dto.ReviewerRouterDto;
import com.chaitu.devpulse.model.DeveloperNode;
import com.chaitu.devpulse.model.FileNode;
import com.chaitu.devpulse.model.PullRequestNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DevPulseGraphService {

    private static final Logger log = LoggerFactory.getLogger(DevPulseGraphService.class);
    private final Neo4jClient neo4jClient;

    @Value("${spring.neo4j.uri:bolt+s://db-7ba26f4a.databases.cognodb.com:7687}")
    private String databaseUri;

    public DevPulseGraphService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public Map<String, Object> seedDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Starting automatic database seed process for CognoDB...");

            // 1. Wipe existing nodes
            neo4jClient.query("MATCH (n) DETACH DELETE n").run();

            // 2. Create Constraints
            try {
                neo4jClient.query("CREATE CONSTRAINT dev_id_unique IF NOT EXISTS FOR (d:Developer) REQUIRE d.id IS UNIQUE").run();
                neo4jClient.query("CREATE CONSTRAINT file_id_unique IF NOT EXISTS FOR (f:File) REQUIRE f.id IS UNIQUE").run();
                neo4jClient.query("CREATE CONSTRAINT pr_id_unique IF NOT EXISTS FOR (pr:PullRequest) REQUIRE pr.id IS UNIQUE").run();
            } catch (Exception e) {
                log.info("Constraint creation warning (already exists): {}", e.getMessage());
            }

            // 3. Create Developers
            neo4jClient.query("CREATE (d1:Developer {id: 'dev_1', name: 'Sarah Jenkins', team: 'Security & Core API', tenure: 'Senior Engineer (4 yrs)', avatarUrl: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80'})").run();
            neo4jClient.query("CREATE (d2:Developer {id: 'dev_2', name: 'Krishna Chaitu', team: 'Backend Architecture', tenure: 'Tech Lead (3 yrs)', avatarUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80'})").run();
            neo4jClient.query("CREATE (d3:Developer {id: 'dev_3', name: 'Alex Rivera', team: 'Payments & Commerce', tenure: 'Senior Backend Dev (2.5 yrs)', avatarUrl: 'https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=300&q=80'})").run();
            neo4jClient.query("CREATE (d4:Developer {id: 'dev_4', name: 'Carlos Mendez', team: 'Database & Infra', tenure: 'Staff Architect (6 yrs)', avatarUrl: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80'})").run();
            neo4jClient.query("CREATE (d5:Developer {id: 'dev_5', name: 'Emily Watson', team: 'Authentication & Identity', tenure: 'Security Engineer (2 yrs)', avatarUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80'})").run();
            neo4jClient.query("CREATE (d6:Developer {id: 'dev_6', name: 'Mike Zhang', team: 'Frontend & Integrations', tenure: 'Junior Engineer (1 yr)', avatarUrl: 'https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=300&q=80'})").run();

            // 4. Create Repositories
            neo4jClient.query("CREATE (r1:Repository {id: 'repo_1', name: 'payment-gateway-service', language: 'Java'})").run();
            neo4jClient.query("CREATE (r2:Repository {id: 'repo_2', name: 'auth-identity-service', language: 'Java'})").run();

            // 5. Create Files
            neo4jClient.query("CREATE (f1:File {id: 'file_1', path: 'OrderService.java', extension: 'java', linesOfCode: 450})").run();
            neo4jClient.query("CREATE (f2:File {id: 'file_2', path: 'PaymentGateway.java', extension: 'java', linesOfCode: 620})").run();
            neo4jClient.query("CREATE (f3:File {id: 'file_3', path: 'AuthCore.java', extension: 'java', linesOfCode: 890})").run();
            neo4jClient.query("CREATE (f4:File {id: 'file_4', path: 'TokenValidator.java', extension: 'java', linesOfCode: 310})").run();
            neo4jClient.query("CREATE (f5:File {id: 'file_5', path: 'V2__payment_schema.sql', extension: 'sql', linesOfCode: 120})").run();
            neo4jClient.query("CREATE (f6:File {id: 'file_6', path: 'CheckoutController.java', extension: 'java', linesOfCode: 280})").run();

            // 6. Create Tags
            neo4jClient.query("CREATE (t1:Tag {id: 'tag_1', name: 'Security', category: 'Domain'})").run();
            neo4jClient.query("CREATE (t2:Tag {id: 'tag_2', name: 'Payments', category: 'Domain'})").run();
            neo4jClient.query("CREATE (t3:Tag {id: 'tag_3', name: 'Database', category: 'Infra'})").run();
            neo4jClient.query("CREATE (t4:Tag {id: 'tag_4', name: 'Auth', category: 'Security'})").run();

            // 7. Create Relationships (DEPENDS_ON)
            neo4jClient.query("MATCH (f1:File {id: 'file_1'}), (f2:File {id: 'file_2'}) CREATE (f1)-[:DEPENDS_ON {type: 'IMPORT'}]->(f2)").run();
            neo4jClient.query("MATCH (f6:File {id: 'file_6'}), (f1:File {id: 'file_1'}) CREATE (f6)-[:DEPENDS_ON {type: 'CALLS'}]->(f1)").run();
            neo4jClient.query("MATCH (f2:File {id: 'file_2'}), (f3:File {id: 'file_3'}) CREATE (f2)-[:DEPENDS_ON {type: 'AUTHENTICATES_VIA'}]->(f3)").run();
            neo4jClient.query("MATCH (f3:File {id: 'file_3'}), (f4:File {id: 'file_4'}) CREATE (f3)-[:DEPENDS_ON {type: 'USES'}]->(f4)").run();
            neo4jClient.query("MATCH (f2:File {id: 'file_2'}), (f5:File {id: 'file_5'}) CREATE (f2)-[:DEPENDS_ON {type: 'PERSISTS_VIA'}]->(f5)").run();

            // 8. Create Pull Requests (PullRequest)
            neo4jClient.query("CREATE (pr45:PullRequest {id: 'pr_45', prNumber: 45, title: 'Refactor OrderService payment flow', status: 'MERGED', createdAt: '2026-08-10T10:00:00Z'})").run();
            neo4jClient.query("CREATE (pr88:PullRequest {id: 'pr_88', prNumber: 88, title: 'Integrate Stripe v2 in PaymentGateway', status: 'MERGED', createdAt: '2026-08-12T14:30:00Z'})").run();
            neo4jClient.query("CREATE (pr102:PullRequest {id: 'pr_102', prNumber: 102, title: 'Harden AuthCore JWT token validation', status: 'MERGED', createdAt: '2026-08-14T09:15:00Z'})").run();
            neo4jClient.query("CREATE (pr105:PullRequest {id: 'pr_105', prNumber: 105, title: 'Optimize V2 payment database indices', status: 'MERGED', createdAt: '2026-08-15T11:20:00Z'})").run();
            neo4jClient.query("CREATE (pr112:PullRequest {id: 'pr_112', prNumber: 112, title: 'Add CheckoutController rate limiting', status: 'OPEN', createdAt: '2026-08-16T16:00:00Z'})").run();

            // 9. Connect PRs to Files (CHANGES)
            neo4jClient.query("MATCH (pr:PullRequest {id: 'pr_45'}), (f:File {id: 'file_1'}) CREATE (pr)-[:CHANGES {additions: 150}]->(f)").run();
            neo4jClient.query("MATCH (pr:PullRequest {id: 'pr_88'}), (f:File {id: 'file_2'}) CREATE (pr)-[:CHANGES {additions: 320}]->(f)").run();
            neo4jClient.query("MATCH (pr:PullRequest {id: 'pr_102'}), (f:File {id: 'file_3'}) CREATE (pr)-[:CHANGES {additions: 210}]->(f)").run();
            neo4jClient.query("MATCH (pr:PullRequest {id: 'pr_102'}), (f:File {id: 'file_4'}) CREATE (pr)-[:CHANGES {additions: 90}]->(f)").run();
            neo4jClient.query("MATCH (pr:PullRequest {id: 'pr_105'}), (f:File {id: 'file_5'}) CREATE (pr)-[:CHANGES {additions: 45}]->(f)").run();
            neo4jClient.query("MATCH (pr:PullRequest {id: 'pr_112'}), (f:File {id: 'file_6'}) CREATE (pr)-[:CHANGES {additions: 110}]->(f)").run();

            // 10. Connect Developers to PRs (CREATED)
            neo4jClient.query("MATCH (d:Developer {id: 'dev_3'}), (pr:PullRequest {id: 'pr_45'}) CREATE (d)-[:CREATED]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_3'}), (pr:PullRequest {id: 'pr_88'}) CREATE (d)-[:CREATED]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_5'}), (pr:PullRequest {id: 'pr_102'}) CREATE (d)-[:CREATED]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_4'}), (pr:PullRequest {id: 'pr_105'}) CREATE (d)-[:CREATED]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_6'}), (pr:PullRequest {id: 'pr_112'}) CREATE (d)-[:CREATED]->(pr)").run();

            // 11. Connect Developers to PRs (REVIEWED)
            neo4jClient.query("MATCH (d:Developer {id: 'dev_1'}), (pr:PullRequest {id: 'pr_45'}) CREATE (d)-[:REVIEWED {score: 95, thoroughness: 'HIGH'}]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_2'}), (pr:PullRequest {id: 'pr_88'}) CREATE (d)-[:REVIEWED {score: 90, thoroughness: 'HIGH'}]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_1'}), (pr:PullRequest {id: 'pr_88'}) CREATE (d)-[:REVIEWED {score: 98, thoroughness: 'CRITICAL'}]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_2'}), (pr:PullRequest {id: 'pr_102'}) CREATE (d)-[:REVIEWED {score: 92, thoroughness: 'HIGH'}]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_4'}), (pr:PullRequest {id: 'pr_102'}) CREATE (d)-[:REVIEWED {score: 85, thoroughness: 'MEDIUM'}]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_2'}), (pr:PullRequest {id: 'pr_105'}) CREATE (d)-[:REVIEWED {score: 88, thoroughness: 'HIGH'}]->(pr)").run();
            neo4jClient.query("MATCH (d:Developer {id: 'dev_3'}), (pr:PullRequest {id: 'pr_112'}) CREATE (d)-[:REVIEWED {score: 75, thoroughness: 'MEDIUM'}]->(pr)").run();

            // 12. Connect Developers to Developers (FOLLOWS)
            neo4jClient.query("MATCH (d2:Developer {id: 'dev_2'}), (d1:Developer {id: 'dev_1'}) CREATE (d2)-[:FOLLOWS]->(d1)").run();
            neo4jClient.query("MATCH (d1:Developer {id: 'dev_1'}), (d4:Developer {id: 'dev_4'}) CREATE (d1)-[:FOLLOWS]->(d4)").run();
            neo4jClient.query("MATCH (d6:Developer {id: 'dev_6'}), (d3:Developer {id: 'dev_3'}) CREATE (d6)-[:FOLLOWS]->(d3)").run();
            neo4jClient.query("MATCH (d3:Developer {id: 'dev_3'}), (d2:Developer {id: 'dev_2'}) CREATE (d3)-[:FOLLOWS]->(d2)").run();

            Long totalNodes = neo4jClient.query("MATCH (n) RETURN count(n) AS count").fetchAs(Long.class).one().orElse(0L);
            Long totalRels = neo4jClient.query("MATCH ()-[r]->() RETURN count(r) AS count").fetchAs(Long.class).one().orElse(0L);

            result.put("seeded", true);
            result.put("message", "Successfully seeded DevPulse graph database into CognoDB Cloud!");
            result.put("nodesCreated", totalNodes);
            result.put("relationshipsCreated", totalRels);

        } catch (Exception ex) {
            log.error("Failed to seed database: {}", ex.getMessage(), ex);
            result.put("seeded", false);
            result.put("error", ex.getMessage());
        }
        return result;
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

    public List<DeveloperNode> getAllDevelopers() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
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
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error fetching developers: {}", ex.getMessage());
            }
        }
        return Collections.emptyList();
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

    public List<PullRequestNode> getAllPullRequests() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
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
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error fetching pull requests: {}", ex.getMessage());
            }
        }
        return Collections.emptyList();
    }

    public ExpertFinderDto findExperts(String query) {
        String cleanQuery = (query == null || query.isBlank()) ? "OrderService.java" : query.trim();

        String cypherPattern = "MATCH (f:File) WHERE toLower(f.path) CONTAINS toLower($q) OR toLower(f.id) CONTAINS toLower($q) " +
                "MATCH (pr:PullRequest)-[:CHANGES]->(f) " +
                "MATCH (dev:Developer)-[r:REVIEWED]->(pr) " +
                "RETURN f.id AS f_id, f.path AS f_path, f.extension AS f_ext, f.linesOfCode AS f_loc, " +
                "dev.id AS d_id, dev.name AS d_name, dev.team AS d_team, dev.tenure AS d_tenure, dev.avatarUrl AS d_avatar, " +
                "r.score AS score, pr.title AS pr_title, pr.prNumber AS pr_num";

        final FileNode[] fileHolder = new FileNode[1];
        Map<String, ExpertFinderDto.ExpertDetail> expertMap = new HashMap<>();

        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                expertMap.clear();
                fileHolder[0] = null;

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
                break;
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error executing findExperts: {}", ex.getMessage());
            }
        }

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

        String cypherPattern = "MATCH (f:File) WHERE toLower(f.path) CONTAINS toLower($file) OR toLower(f.id) CONTAINS toLower($file) " +
                "MATCH (pr:PullRequest)-[:CHANGES]->(f) " +
                "MATCH (dev:Developer)-[r:REVIEWED]->(pr) " +
                "RETURN dev.id AS d_id, dev.name AS d_name, dev.team AS d_team, dev.tenure AS d_tenure, dev.avatarUrl AS d_avatar, " +
                "count(pr) AS reviewCount, avg(r.score) AS avgScore " +
                "ORDER BY reviewCount DESC, avgScore DESC LIMIT 3";

        List<ReviewerRouterDto.ReviewerRecommendation> list = new ArrayList<>();

        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                list.clear();
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
                            String reason = "Reviewed " + count + " PRs touching this file or its dependency hierarchy.";

                            list.add(new ReviewerRouterDto.ReviewerRecommendation(dev, score, reason, count));
                            return null;
                        })
                        .all();
                break;
            } catch (Exception ex) {
                if (attempt == 2) log.error("Error executing recommendReviewers: {}", ex.getMessage());
            }
        }

        return new ReviewerRouterDto(cleanPath, "PR Review Recommendation for " + cleanPath, list, cypherPattern);
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

        return new HealthRadarDto(modules, modules.size(), cypherPattern);
    }
}
