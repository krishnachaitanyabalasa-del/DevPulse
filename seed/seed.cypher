// DevPulse - Production-Grade Developer Knowledge & Code Review Graph Seed Script
MATCH (n) DETACH DELETE n;

// 1. Create Constraints
CREATE CONSTRAINT dev_id_unique IF NOT EXISTS FOR (d:Developer) REQUIRE d.id IS UNIQUE;
CREATE CONSTRAINT repo_id_unique IF NOT EXISTS FOR (r:Repository) REQUIRE r.id IS UNIQUE;
CREATE CONSTRAINT pr_id_unique IF NOT EXISTS FOR (pr:PullRequest) REQUIRE pr.id IS UNIQUE;
CREATE CONSTRAINT file_id_unique IF NOT EXISTS FOR (f:File) REQUIRE f.id IS UNIQUE;
CREATE CONSTRAINT tag_id_unique IF NOT EXISTS FOR (t:Tag) REQUIRE t.id IS UNIQUE;

// 2. Create 10 Developers across Engineering Org
CREATE (d1:Developer {id: "dev_1", name: "Sarah Jenkins", team: "Security & Core API", tenure: "Senior Engineer (4 yrs)", avatarUrl: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80"})
CREATE (d2:Developer {id: "dev_2", name: "Krishna Chaitu", team: "Backend Architecture", tenure: "Tech Lead (3 yrs)", avatarUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80"})
CREATE (d3:Developer {id: "dev_3", name: "Alex Rivera", team: "Payments & Commerce", tenure: "Senior Backend Dev (2.5 yrs)", avatarUrl: "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=300&q=80"})
CREATE (d4:Developer {id: "dev_4", name: "Carlos Mendez", team: "Database & Infra", tenure: "Staff Architect (6 yrs)", avatarUrl: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80"})
CREATE (d5:Developer {id: "dev_5", name: "Emily Watson", team: "Authentication & Identity", tenure: "Security Engineer (2 yrs)", avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"})
CREATE (d6:Developer {id: "dev_6", name: "Mike Zhang", team: "Frontend & Integrations", tenure: "Junior Engineer (1 yr)", avatarUrl: "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=300&q=80"})
CREATE (d7:Developer {id: "dev_7", name: "Priya Patel", team: "Cloud & DevOps", tenure: "Principal SRE (5 yrs)", avatarUrl: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=300&q=80"})
CREATE (d8:Developer {id: "dev_8", name: "David Kim", team: "API Gateway & Services", tenure: "Staff Engineer (4 yrs)", avatarUrl: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=300&q=80"})
CREATE (d9:Developer {id: "dev_9", name: "Hannah Abbott", team: "Data & Analytics", tenure: "Senior Data Engineer (3 yrs)", avatarUrl: "https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=300&q=80"})
CREATE (d10:Developer {id: "dev_10", name: "Lucas Vance", team: "Platform & Performance", tenure: "Principal Engineer (7 yrs)", avatarUrl: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=300&q=80"});

// 3. Create 5 Repositories
CREATE (r1:Repository {id: "repo_1", name: "payment-gateway-service", language: "Java"})
CREATE (r2:Repository {id: "repo_2", name: "auth-identity-service", language: "Java"})
CREATE (r3:Repository {id: "repo_3", name: "core-api-service", language: "Java"})
CREATE (r4:Repository {id: "repo_4", name: "infrastructure-config", language: "HCL"})
CREATE (r5:Repository {id: "repo_5", name: "data-analytics-pipeline", language: "Python"});

// 4. Create 12 File Modules
CREATE (f1:File {id: "file_1", path: "OrderService.java", extension: "java", linesOfCode: 450})
CREATE (f2:File {id: "file_2", path: "PaymentGateway.java", extension: "java", linesOfCode: 620})
CREATE (f3:File {id: "file_3", path: "AuthCore.java", extension: "java", linesOfCode: 890})
CREATE (f4:File {id: "file_4", path: "TokenValidator.java", extension: "java", linesOfCode: 310})
CREATE (f5:File {id: "file_5", path: "V2__payment_schema.sql", extension: "sql", linesOfCode: 120})
CREATE (f6:File {id: "file_6", path: "CheckoutController.java", extension: "java", linesOfCode: 280})
CREATE (f7:File {id: "file_7", path: "StripeClient.java", extension: "java", linesOfCode: 510})
CREATE (f8:File {id: "file_8", path: "JWTUtils.java", extension: "java", linesOfCode: 340})
CREATE (f9:File {id: "file_9", path: "AuditLogger.java", extension: "java", linesOfCode: 230})
CREATE (f10:File {id: "file_10", path: "SecurityConfig.java", extension: "java", linesOfCode: 410})
CREATE (f11:File {id: "file_11", path: "RateLimiter.java", extension: "java", linesOfCode: 290})
CREATE (f12:File {id: "file_12", path: "DatabasePoolConfig.java", extension: "java", linesOfCode: 180});

// 5. Create Domain Tags
CREATE (t1:Tag {id: "tag_1", name: "Security", category: "Domain"})
CREATE (t2:Tag {id: "tag_2", name: "Payments", category: "Domain"})
CREATE (t3:Tag {id: "tag_3", name: "Database", category: "Infra"})
CREATE (t4:Tag {id: "tag_4", name: "Auth", category: "Security"})
CREATE (t5:Tag {id: "tag_5", name: "Performance", category: "Optimization"})
CREATE (t6:Tag {id: "tag_6", name: "DevOps", category: "Infra"});

// 6. Create Architectural Code Dependencies (DEPENDS_ON)
CREATE (f1)-[:DEPENDS_ON {type: "IMPORT"}]->(f2)
CREATE (f6)-[:DEPENDS_ON {type: "CALLS"}]->(f1)
CREATE (f2)-[:DEPENDS_ON {type: "AUTHENTICATES_VIA"}]->(f3)
CREATE (f2)-[:DEPENDS_ON {type: "DELEGATES_TO"}]->(f7)
CREATE (f3)-[:DEPENDS_ON {type: "USES"}]->(f4)
CREATE (f3)-[:DEPENDS_ON {type: "USES"}]->(f8)
CREATE (f2)-[:DEPENDS_ON {type: "PERSISTS_VIA"}]->(f5)
CREATE (f10)-[:DEPENDS_ON {type: "CONFIGURES"}]->(f3)
CREATE (f6)-[:DEPENDS_ON {type: "PROTECTED_BY"}]->(f11)
CREATE (f5)-[:DEPENDS_ON {type: "MANAGED_BY"}]->(f12)
CREATE (f3)-[:DEPENDS_ON {type: "AUDITS_TO"}]->(f9);

// 7. Create Pull Requests
CREATE (pr45:PullRequest {id: "pr_45", prNumber: 45, title: "Refactor OrderService payment flow", status: "MERGED", createdAt: "2026-08-10T10:00:00Z"})
CREATE (pr88:PullRequest {id: "pr_88", prNumber: 88, title: "Integrate Stripe v2 in PaymentGateway", status: "MERGED", createdAt: "2026-08-12T14:30:00Z"})
CREATE (pr102:PullRequest {id: "pr_102", prNumber: 102, title: "Harden AuthCore JWT token validation", status: "MERGED", createdAt: "2026-08-14T09:15:00Z"})
CREATE (pr105:PullRequest {id: "pr_105", prNumber: 105, title: "Optimize V2 payment database indices", status: "MERGED", createdAt: "2026-08-15T11:20:00Z"})
CREATE (pr112:PullRequest {id: "pr_112", prNumber: 112, title: "Add CheckoutController rate limiting", status: "OPEN", createdAt: "2026-08-16T16:00:00Z"})
CREATE (pr120:PullRequest {id: "pr_120", prNumber: 120, title: "Upgrade Stripe SDK to 2026 spec", status: "MERGED", createdAt: "2026-08-17T08:45:00Z"})
CREATE (pr125:PullRequest {id: "pr_125", prNumber: 125, title: "Add SecurityConfig CSRF & OAuth2 rules", status: "MERGED", createdAt: "2026-08-18T13:10:00Z"})
CREATE (pr130:PullRequest {id: "pr_130", prNumber: 130, title: "Tune DatabasePoolConfig HikariCP settings", status: "OPEN", createdAt: "2026-08-19T07:00:00Z"});

// 8. Connect PRs to Files (CHANGES)
CREATE (pr45)-[:CHANGES {additions: 150}]->(f1)
CREATE (pr88)-[:CHANGES {additions: 320}]->(f2)
CREATE (pr102)-[:CHANGES {additions: 210}]->(f3)
CREATE (pr102)-[:CHANGES {additions: 90}]->(f4)
CREATE (pr105)-[:CHANGES {additions: 45}]->(f5)
CREATE (pr112)-[:CHANGES {additions: 110}]->(f6)
CREATE (pr120)-[:CHANGES {additions: 240}]->(f7)
CREATE (pr125)-[:CHANGES {additions: 180}]->(f10)
CREATE (pr130)-[:CHANGES {additions: 65}]->(f12);

// 9. Connect PRs to Tags
CREATE (pr45)-[:TAGGED_WITH]->(t2)
CREATE (pr88)-[:TAGGED_WITH]->(t2)
CREATE (pr102)-[:TAGGED_WITH]->(t1)
CREATE (pr102)-[:TAGGED_WITH]->(t4)
CREATE (pr105)-[:TAGGED_WITH]->(t3)
CREATE (pr112)-[:TAGGED_WITH]->(t5)
CREATE (pr125)-[:TAGGED_WITH]->(t1)
CREATE (pr130)-[:TAGGED_WITH]->(t3);

// 10. Developer Authorship (CREATED)
CREATE (d3)-[:CREATED]->(pr45)
CREATE (d3)-[:CREATED]->(pr88)
CREATE (d5)-[:CREATED]->(pr102)
CREATE (d4)-[:CREATED]->(pr105)
CREATE (d6)-[:CREATED]->(pr112)
CREATE (d3)-[:CREATED]->(pr120)
CREATE (d1)-[:CREATED]->(pr125)
CREATE (d7)-[:CREATED]->(pr130);

// 11. Code Review History (REVIEWED) - Weighted review history!
CREATE (d1)-[:REVIEWED {score: 95, thoroughness: "HIGH"}]->(pr45)
CREATE (d2)-[:REVIEWED {score: 90, thoroughness: "HIGH"}]->(pr88)
CREATE (d1)-[:REVIEWED {score: 98, thoroughness: "CRITICAL"}]->(pr88)
CREATE (d2)-[:REVIEWED {score: 92, thoroughness: "HIGH"}]->(pr102)
CREATE (d4)-[:REVIEWED {score: 85, thoroughness: "MEDIUM"}]->(pr102)
CREATE (d2)-[:REVIEWED {score: 88, thoroughness: "HIGH"}]->(pr105)
CREATE (d3)-[:REVIEWED {score: 75, thoroughness: "MEDIUM"}]->(pr112)
CREATE (d2)-[:REVIEWED {score: 94, thoroughness: "HIGH"}]->(pr120)
CREATE (d5)-[:REVIEWED {score: 96, thoroughness: "CRITICAL"}]->(pr125)
CREATE (d4)-[:REVIEWED {score: 91, thoroughness: "HIGH"}]->(pr130);

// 12. Developer Mentorship & Social Graph (FOLLOWS - 2+ Hop Paths)
CREATE (d2)-[:FOLLOWS]->(d1)
CREATE (d1)-[:FOLLOWS]->(d4)
CREATE (d4)-[:FOLLOWS]->(d10)
CREATE (d6)-[:FOLLOWS]->(d3)
CREATE (d3)-[:FOLLOWS]->(d2)
CREATE (d5)-[:FOLLOWS]->(d1)
CREATE (d8)-[:FOLLOWS]->(d2)
CREATE (d7)-[:FOLLOWS]->(d4)
CREATE (d9)-[:FOLLOWS]->(d2);
