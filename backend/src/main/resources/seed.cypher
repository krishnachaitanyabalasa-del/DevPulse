// DevPulse - Production-Grade Developer Knowledge & Code Review Graph Seed Script
MATCH (n) DETACH DELETE n;

// 1. Create Developers
CREATE (d:Developer {id: "dev_1", name: "Sarah Jenkins", team: "Security & Core API", tenure: "Senior Engineer (4 yrs)", avatarUrl: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_2", name: "Krishna Chaitu", team: "Backend Architecture", tenure: "Tech Lead (3 yrs)", avatarUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_3", name: "Alex Rivera", team: "Payments & Commerce", tenure: "Senior Backend Dev (2.5 yrs)", avatarUrl: "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_4", name: "Carlos Mendez", team: "Database & Infra", tenure: "Staff Architect (6 yrs)", avatarUrl: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_5", name: "Emily Watson", team: "Authentication & Identity", tenure: "Security Engineer (2 yrs)", avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_6", name: "Mike Zhang", team: "Frontend & Integrations", tenure: "Junior Engineer (1 yr)", avatarUrl: "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_7", name: "Priya Patel", team: "Cloud & DevOps", tenure: "Principal SRE (5 yrs)", avatarUrl: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_8", name: "David Kim", team: "API Gateway & Services", tenure: "Staff Engineer (4 yrs)", avatarUrl: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_9", name: "Hannah Abbott", team: "Data & Analytics", tenure: "Senior Data Engineer (3 yrs)", avatarUrl: "https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=300&q=80"});
CREATE (d:Developer {id: "dev_10", name: "Lucas Vance", team: "Platform & Performance", tenure: "Principal Engineer (7 yrs)", avatarUrl: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=300&q=80"});

// 2. Create Repositories
CREATE (r:Repository {id: "repo_1", name: "payment-gateway-service", language: "Java"});
CREATE (r:Repository {id: "repo_2", name: "auth-identity-service", language: "Java"});
CREATE (r:Repository {id: "repo_3", name: "core-api-service", language: "Java"});
CREATE (r:Repository {id: "repo_4", name: "infrastructure-config", language: "HCL"});
CREATE (r:Repository {id: "repo_5", name: "data-analytics-pipeline", language: "Python"});

// 3. Create Files
CREATE (f:File {id: "file_1", path: "OrderService.java", extension: "java", linesOfCode: 450});
CREATE (f:File {id: "file_2", path: "PaymentGateway.java", extension: "java", linesOfCode: 620});
CREATE (f:File {id: "file_3", path: "AuthCore.java", extension: "java", linesOfCode: 890});
CREATE (f:File {id: "file_4", path: "TokenValidator.java", extension: "java", linesOfCode: 310});
CREATE (f:File {id: "file_5", path: "V2__payment_schema.sql", extension: "sql", linesOfCode: 120});
CREATE (f:File {id: "file_6", path: "CheckoutController.java", extension: "java", linesOfCode: 280});
CREATE (f:File {id: "file_7", path: "StripeClient.java", extension: "java", linesOfCode: 510});
CREATE (f:File {id: "file_8", path: "JWTUtils.java", extension: "java", linesOfCode: 340});
CREATE (f:File {id: "file_9", path: "AuditLogger.java", extension: "java", linesOfCode: 230});
CREATE (f:File {id: "file_10", path: "SecurityConfig.java", extension: "java", linesOfCode: 410});
CREATE (f:File {id: "file_11", path: "RateLimiter.java", extension: "java", linesOfCode: 290});
CREATE (f:File {id: "file_12", path: "DatabasePoolConfig.java", extension: "java", linesOfCode: 180});

// 4. Create Tags
CREATE (t:Tag {id: "tag_1", name: "Security", category: "Domain"});
CREATE (t:Tag {id: "tag_2", name: "Payments", category: "Domain"});
CREATE (t:Tag {id: "tag_3", name: "Database", category: "Infra"});
CREATE (t:Tag {id: "tag_4", name: "Auth", category: "Security"});
CREATE (t:Tag {id: "tag_5", name: "Performance", category: "Optimization"});
CREATE (t:Tag {id: "tag_6", name: "DevOps", category: "Infra"});

// 5. Create Pull Requests
CREATE (pr:PullRequest {id: "pr_45", prNumber: 45, title: "Refactor OrderService payment flow", status: "MERGED", createdAt: "2026-08-10T10:00:00Z"});
CREATE (pr:PullRequest {id: "pr_88", prNumber: 88, title: "Integrate Stripe v2 in PaymentGateway", status: "MERGED", createdAt: "2026-08-12T14:30:00Z"});
CREATE (pr:PullRequest {id: "pr_102", prNumber: 102, title: "Harden AuthCore JWT token validation", status: "MERGED", createdAt: "2026-08-14T09:15:00Z"});
CREATE (pr:PullRequest {id: "pr_105", prNumber: 105, title: "Optimize V2 payment database indices", status: "MERGED", createdAt: "2026-08-15T11:20:00Z"});
CREATE (pr:PullRequest {id: "pr_112", prNumber: 112, title: "Add CheckoutController rate limiting", status: "OPEN", createdAt: "2026-08-16T16:00:00Z"});
CREATE (pr:PullRequest {id: "pr_120", prNumber: 120, title: "Upgrade Stripe SDK to 2026 spec", status: "MERGED", createdAt: "2026-08-17T08:45:00Z"});
CREATE (pr:PullRequest {id: "pr_125", prNumber: 125, title: "Add SecurityConfig CSRF & OAuth2 rules", status: "MERGED", createdAt: "2026-08-18T13:10:00Z"});
CREATE (pr:PullRequest {id: "pr_130", prNumber: 130, title: "Tune DatabasePoolConfig HikariCP settings", status: "OPEN", createdAt: "2026-08-19T07:00:00Z"});

// 6. Code Dependencies (DEPENDS_ON)
MATCH (a:File {id: "file_1"}), (b:File {id: "file_2"}) CREATE (a)-[:DEPENDS_ON {type: "IMPORT"}]->(b);
MATCH (a:File {id: "file_6"}), (b:File {id: "file_1"}) CREATE (a)-[:DEPENDS_ON {type: "CALLS"}]->(b);
MATCH (a:File {id: "file_2"}), (b:File {id: "file_3"}) CREATE (a)-[:DEPENDS_ON {type: "AUTHENTICATES_VIA"}]->(b);
MATCH (a:File {id: "file_2"}), (b:File {id: "file_7"}) CREATE (a)-[:DEPENDS_ON {type: "DELEGATES_TO"}]->(b);
MATCH (a:File {id: "file_3"}), (b:File {id: "file_4"}) CREATE (a)-[:DEPENDS_ON {type: "USES"}]->(b);
MATCH (a:File {id: "file_3"}), (b:File {id: "file_8"}) CREATE (a)-[:DEPENDS_ON {type: "USES"}]->(b);
MATCH (a:File {id: "file_2"}), (b:File {id: "file_5"}) CREATE (a)-[:DEPENDS_ON {type: "PERSISTS_VIA"}]->(b);
MATCH (a:File {id: "file_10"}), (b:File {id: "file_3"}) CREATE (a)-[:DEPENDS_ON {type: "CONFIGURES"}]->(b);
MATCH (a:File {id: "file_6"}), (b:File {id: "file_11"}) CREATE (a)-[:DEPENDS_ON {type: "PROTECTED_BY"}]->(b);
MATCH (a:File {id: "file_5"}), (b:File {id: "file_12"}) CREATE (a)-[:DEPENDS_ON {type: "MANAGED_BY"}]->(b);
MATCH (a:File {id: "file_3"}), (b:File {id: "file_9"}) CREATE (a)-[:DEPENDS_ON {type: "AUDITS_TO"}]->(b);

// 7. PR File Modifications (CHANGES)
MATCH (pr:PullRequest {id: "pr_45"}), (f:File {id: "file_1"}) CREATE (pr)-[:CHANGES {additions: 150}]->(f);
MATCH (pr:PullRequest {id: "pr_88"}), (f:File {id: "file_2"}) CREATE (pr)-[:CHANGES {additions: 320}]->(f);
MATCH (pr:PullRequest {id: "pr_102"}), (f:File {id: "file_3"}) CREATE (pr)-[:CHANGES {additions: 210}]->(f);
MATCH (pr:PullRequest {id: "pr_102"}), (f:File {id: "file_4"}) CREATE (pr)-[:CHANGES {additions: 90}]->(f);
MATCH (pr:PullRequest {id: "pr_105"}), (f:File {id: "file_5"}) CREATE (pr)-[:CHANGES {additions: 45}]->(f);
MATCH (pr:PullRequest {id: "pr_112"}), (f:File {id: "file_6"}) CREATE (pr)-[:CHANGES {additions: 110}]->(f);
MATCH (pr:PullRequest {id: "pr_120"}), (f:File {id: "file_7"}) CREATE (pr)-[:CHANGES {additions: 240}]->(f);
MATCH (pr:PullRequest {id: "pr_125"}), (f:File {id: "file_10"}) CREATE (pr)-[:CHANGES {additions: 180}]->(f);
MATCH (pr:PullRequest {id: "pr_130"}), (f:File {id: "file_12"}) CREATE (pr)-[:CHANGES {additions: 65}]->(f);

// 8. PR Tags (TAGGED_WITH)
MATCH (pr:PullRequest {id: "pr_45"}), (t:Tag {id: "tag_2"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_88"}), (t:Tag {id: "tag_2"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_102"}), (t:Tag {id: "tag_1"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_102"}), (t:Tag {id: "tag_4"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_105"}), (t:Tag {id: "tag_3"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_112"}), (t:Tag {id: "tag_5"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_125"}), (t:Tag {id: "tag_1"}) CREATE (pr)-[:TAGGED_WITH]->(t);
MATCH (pr:PullRequest {id: "pr_130"}), (t:Tag {id: "tag_3"}) CREATE (pr)-[:TAGGED_WITH]->(t);

// 9. Developer Authorship (CREATED)
MATCH (d:Developer {id: "dev_3"}), (pr:PullRequest {id: "pr_45"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_3"}), (pr:PullRequest {id: "pr_88"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_5"}), (pr:PullRequest {id: "pr_102"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_4"}), (pr:PullRequest {id: "pr_105"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_6"}), (pr:PullRequest {id: "pr_112"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_3"}), (pr:PullRequest {id: "pr_120"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_1"}), (pr:PullRequest {id: "pr_125"}) CREATE (d)-[:CREATED]->(pr);
MATCH (d:Developer {id: "dev_7"}), (pr:PullRequest {id: "pr_130"}) CREATE (d)-[:CREATED]->(pr);

// 10. Code Review History (REVIEWED)
MATCH (d:Developer {id: "dev_1"}), (pr:PullRequest {id: "pr_45"}) CREATE (d)-[:REVIEWED {score: 95, thoroughness: "HIGH"}]->(pr);
MATCH (d:Developer {id: "dev_2"}), (pr:PullRequest {id: "pr_88"}) CREATE (d)-[:REVIEWED {score: 90, thoroughness: "HIGH"}]->(pr);
MATCH (d:Developer {id: "dev_1"}), (pr:PullRequest {id: "pr_88"}) CREATE (d)-[:REVIEWED {score: 98, thoroughness: "CRITICAL"}]->(pr);
MATCH (d:Developer {id: "dev_2"}), (pr:PullRequest {id: "pr_102"}) CREATE (d)-[:REVIEWED {score: 92, thoroughness: "HIGH"}]->(pr);
MATCH (d:Developer {id: "dev_4"}), (pr:PullRequest {id: "pr_102"}) CREATE (d)-[:REVIEWED {score: 85, thoroughness: "MEDIUM"}]->(pr);
MATCH (d:Developer {id: "dev_2"}), (pr:PullRequest {id: "pr_105"}) CREATE (d)-[:REVIEWED {score: 88, thoroughness: "HIGH"}]->(pr);
MATCH (d:Developer {id: "dev_3"}), (pr:PullRequest {id: "pr_112"}) CREATE (d)-[:REVIEWED {score: 75, thoroughness: "MEDIUM"}]->(pr);
MATCH (d:Developer {id: "dev_2"}), (pr:PullRequest {id: "pr_120"}) CREATE (d)-[:REVIEWED {score: 94, thoroughness: "HIGH"}]->(pr);
MATCH (d:Developer {id: "dev_5"}), (pr:PullRequest {id: "pr_125"}) CREATE (d)-[:REVIEWED {score: 96, thoroughness: "CRITICAL"}]->(pr);
MATCH (d:Developer {id: "dev_4"}), (pr:PullRequest {id: "pr_130"}) CREATE (d)-[:REVIEWED {score: 91, thoroughness: "HIGH"}]->(pr);

// 11. Developer Social Mentorship Graph (FOLLOWS)
MATCH (a:Developer {id: "dev_2"}), (b:Developer {id: "dev_1"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_1"}), (b:Developer {id: "dev_4"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_4"}), (b:Developer {id: "dev_10"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_6"}), (b:Developer {id: "dev_3"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_3"}), (b:Developer {id: "dev_2"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_5"}), (b:Developer {id: "dev_1"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_8"}), (b:Developer {id: "dev_2"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_7"}), (b:Developer {id: "dev_4"}) CREATE (a)-[:FOLLOWS]->(b);
MATCH (a:Developer {id: "dev_9"}), (b:Developer {id: "dev_2"}) CREATE (a)-[:FOLLOWS]->(b);
