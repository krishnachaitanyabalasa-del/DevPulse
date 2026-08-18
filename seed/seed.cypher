// DevPulse - Developer Knowledge & Code Review Graph Seed Script
MATCH (n) DETACH DELETE n;

// 1. Create Constraints & Indexes
CREATE CONSTRAINT dev_id_unique IF NOT EXISTS FOR (d:Developer) REQUIRE d.id IS UNIQUE;
CREATE CONSTRAINT dev_name_unique IF NOT EXISTS FOR (d:Developer) REQUIRE d.name IS UNIQUE;
CREATE CONSTRAINT repo_id_unique IF NOT EXISTS FOR (r:Repository) REQUIRE r.id IS UNIQUE;
CREATE CONSTRAINT pr_id_unique IF NOT EXISTS FOR (pr:PullRequest) REQUIRE pr.id IS UNIQUE;
CREATE CONSTRAINT file_id_unique IF NOT EXISTS FOR (f:File) REQUIRE f.id IS UNIQUE;
CREATE CONSTRAINT tag_id_unique IF NOT EXISTS FOR (t:Tag) REQUIRE t.id IS UNIQUE;

// 2. Create Developers
CREATE (d_sarah:Developer {
    id: "dev_1", 
    name: "Sarah Jenkins", 
    team: "Security & Core API", 
    tenure: "Senior Engineer (4 yrs)",
    avatarUrl: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80"
})
CREATE (d_chaitu:Developer {
    id: "dev_2", 
    name: "Krishna Chaitu", 
    team: "Backend Architecture", 
    tenure: "Tech Lead (3 yrs)",
    avatarUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80"
})
CREATE (d_alex:Developer {
    id: "dev_3", 
    name: "Alex Rivera", 
    team: "Payments & Commerce", 
    tenure: "Senior Backend Dev (2.5 yrs)",
    avatarUrl: "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=300&q=80"
})
CREATE (d_carlos:Developer {
    id: "dev_4", 
    name: "Carlos Mendez", 
    team: "Database & Infra", 
    tenure: "Staff Architect (6 yrs)",
    avatarUrl: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80"
})
CREATE (d_emily:Developer {
    id: "dev_5", 
    name: "Emily Watson", 
    team: "Authentication & Identity", 
    tenure: "Security Engineer (2 yrs)",
    avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
})
CREATE (d_mike:Developer {
    id: "dev_6", 
    name: "Mike Zhang", 
    team: "Frontend & Integrations", 
    tenure: "Junior Engineer (1 yr)",
    avatarUrl: "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=300&q=80"
})

// 3. Create Repositories
CREATE (r_payments:Repository {id: "repo_1", name: "payment-gateway-service", language: "Java"})
CREATE (r_auth:Repository {id: "repo_2", name: "auth-identity-service", language: "Java"})

// 4. Create Files
CREATE (f_orderservice:File {id: "file_1", path: "OrderService.java", extension: "java", linesOfCode: 450})
CREATE (f_paymentgateway:File {id: "file_2", path: "PaymentGateway.java", extension: "java", linesOfCode: 620})
CREATE (f_authcore:File {id: "file_3", path: "AuthCore.java", extension: "java", linesOfCode: 890})
CREATE (f_tokenval:File {id: "file_4", path: "TokenValidator.java", extension: "java", linesOfCode: 310})
CREATE (f_dbmigration:File {id: "file_5", path: "V2__payment_schema.sql", extension: "sql", linesOfCode: 120})
CREATE (f_checkoutctrl:File {id: "file_6", path: "CheckoutController.java", extension: "java", linesOfCode: 280})

// 5. Create Tags
CREATE (t_security:Tag {id: "tag_1", name: "Security", category: "Domain"})
CREATE (t_payments:Tag {id: "tag_2", name: "Payments", category: "Domain"})
CREATE (t_database:Tag {id: "tag_3", name: "Database", category: "Infra"})
CREATE (t_auth:Tag {id: "tag_4", name: "Auth", category: "Security"})

// 6. Create File-Level Dependencies (DEPENDS_ON) - Code Architecture Graph!
CREATE (f_orderservice)-[:DEPENDS_ON {type: "IMPORT"}]->(f_paymentgateway)
CREATE (f_checkoutctrl)-[:DEPENDS_ON {type: "CALLS"}]->(f_orderservice)
CREATE (f_paymentgateway)-[:DEPENDS_ON {type: "AUTHENTICATES_VIA"}]->(f_authcore)
CREATE (f_authcore)-[:DEPENDS_ON {type: "USES"}]->(f_tokenval)
CREATE (f_paymentgateway)-[:DEPENDS_ON {type: "PERSISTS_VIA"}]->(f_dbmigration)

// 7. Create Pull Requests (PullRequest)
CREATE (pr_45:PullRequest {id: "pr_45", prNumber: 45, title: "Refactor OrderService payment flow", status: "MERGED", createdAt: "2026-08-10T10:00:00Z"})
CREATE (pr_88:PullRequest {id: "pr_88", prNumber: 88, title: "Integrate Stripe v2 in PaymentGateway", status: "MERGED", createdAt: "2026-08-12T14:30:00Z"})
CREATE (pr_102:PullRequest {id: "pr_102", prNumber: 102, title: "Harden AuthCore JWT token validation", status: "MERGED", createdAt: "2026-08-14T09:15:00Z"})
CREATE (pr_105:PullRequest {id: "pr_105", prNumber: 105, title: "Optimize V2 payment database indices", status: "MERGED", createdAt: "2026-08-15T11:20:00Z"})
CREATE (pr_112:PullRequest {id: "pr_112", prNumber: 112, title: "Add CheckoutController rate limiting", status: "OPEN", createdAt: "2026-08-16T16:00:00Z"})

// 8. Connect PRs to Files (CHANGES)
CREATE (pr_45)-[:CHANGES {additions: 150}]->(f_orderservice)
CREATE (pr_88)-[:CHANGES {additions: 320}]->(f_paymentgateway)
CREATE (pr_102)-[:CHANGES {additions: 210}]->(f_authcore)
CREATE (pr_102)-[:CHANGES {additions: 90}]->(f_tokenval)
CREATE (pr_105)-[:CHANGES {additions: 45}]->(f_dbmigration)
CREATE (pr_112)-[:CHANGES {additions: 110}]->(f_checkoutctrl)

// 9. Connect PRs to Tags (TAGGED_WITH)
CREATE (pr_45)-[:TAGGED_WITH]->(t_payments)
CREATE (pr_88)-[:TAGGED_WITH]->(t_payments)
CREATE (pr_102)-[:TAGGED_WITH]->(t_security)
CREATE (pr_102)-[:TAGGED_WITH]->(t_auth)
CREATE (pr_105)-[:TAGGED_WITH]->(t_database)

// 10. Connect Developers to PRs (CREATED)
CREATE (d_alex)-[:CREATED]->(pr_45)
CREATE (d_alex)-[:CREATED]->(pr_88)
CREATE (d_emily)-[:CREATED]->(pr_102)
CREATE (d_carlos)-[:CREATED]->(pr_105)
CREATE (d_mike)-[:CREATED]->(pr_112)

// 11. Connect Developers to PRs (REVIEWED) - Weighted review history!
CREATE (d_sarah)-[:REVIEWED {score: 95, thoroughness: "HIGH"}]->(pr_45)
CREATE (d_chaitu)-[:REVIEWED {score: 90, thoroughness: "HIGH"}]->(pr_88)
CREATE (d_sarah)-[:REVIEWED {score: 98, thoroughness: "CRITICAL"}]->(pr_88)
CREATE (d_chaitu)-[:REVIEWED {score: 92, thoroughness: "HIGH"}]->(pr_102)
CREATE (d_carlos)-[:REVIEWED {score: 85, thoroughness: "MEDIUM"}]->(pr_102)
CREATE (d_chaitu)-[:REVIEWED {score: 88, thoroughness: "HIGH"}]->(pr_105)
CREATE (d_alex)-[:REVIEWED {score: 75, thoroughness: "MEDIUM"}]->(pr_112)

// 12. Connect Developers to Developers (FOLLOWS / Mentorship)
CREATE (d_chaitu)-[:FOLLOWS]->(d_sarah)
CREATE (d_sarah)-[:FOLLOWS]->(d_carlos)
CREATE (d_mike)-[:FOLLOWS]->(d_alex)
CREATE (d_alex)-[:FOLLOWS]->(d_chaitu);
