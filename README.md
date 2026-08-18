# DevPulse — Developer Knowledge & Code Review Graph Engine

**DevPulse** is a production-grade Graph Database application built for engineering teams to eliminate tribal knowledge black holes, intelligently route code reviews, and detect single-point-of-failure "Bus Factor" risks in codebases.

Built using **Java 21**, **Spring Boot 3**, **Spring Data Neo4j**, and **React 18 + Vite**, backed by **CognoDB Cloud** (Neo4j driver compatible) as the graph database layer.

---

## 🎯 1. The Real-World Problem (The "Why")

In software engineering organizations with 20+ developers, three silent productivity killers exist:

1. **The "Tribal Knowledge" Black Hole**: When a developer joins, they ask *"Who knows how PaymentGateway works?"* The answer is usually a guessing game or an ignored Slack message.
2. **The Blind Code Review**: Pull Requests (PRs) are assigned randomly. A frontend developer gets assigned to review a complex database migration, leading to slow turnarounds and missed bugs.
3. **The Hidden Bus Factor**: Teams don't realize that only *one* person has reviewed/authored the core authentication module in the last 2 years. If they leave, the team is paralyzed.

Existing ticket trackers (Jira/Linear) and version control hosts (GitHub) track *commits* and *tickets*, but neither tracks the **weighted relationships between developers, code files, pull requests, and sub-dependencies**.

---

## ⚡ 2. The Solution (DevPulse)

DevPulse ingests GitHub metadata into a graph network map that answers questions traditional SQL databases find awkward:

> *"Not just who wrote this file, but who understands the ripple effect of changing it?"*

### 3 Core Tools in DevPulse:
- **The "Expert Finder"**: Type a file path (e.g. `OrderService.java`) or tech tag (e.g., `#Security`, `#Payments`). DevPulse maps a **3-hop graph path** from that file to developers who reviewed PRs affecting it and their team proximity.
- **The "Smart Reviewer Router"**: Evaluates PRs before merge and recommends the top 3 best-fit reviewers ranked by contextual relevance (review history + file dependency proximity).
- **The "Health Radar"**: Graph centrality dashboard highlighting critical files with high dependency in-degree but low developer density (identifying Bus Factor risks).

---

## 🧠 3. Why a Graph Database? (Graph vs. Relational SQL)

If we built DevPulse in PostgreSQL or MySQL, we would need 3 massive join tables (`PR_Reviewers`, `PR_Files`, `File_Dependencies`) and complex **Recursive Common Table Expressions (CTEs)**.

### The Relational SQL Nightmare
To find *"developers who reviewed PRs touching files that depend on PaymentGateway.java"* in SQL:

```sql
-- Relational SQL: Clunky 6-table join with recursive CTE
WITH RECURSIVE FileDependencies AS (
    SELECT file_id, depends_on_file_id FROM file_deps WHERE file_id = 'file_2'
    UNION ALL
    SELECT fd.file_id, d.depends_on_file_id 
    FROM file_deps d JOIN FileDependencies fd ON d.file_id = fd.depends_on_file_id
)
SELECT dev.name, COUNT(pr.id) AS review_count, AVG(pr_rev.score) AS avg_score
FROM FileDependencies fd
JOIN pr_files prf ON prf.file_id = fd.depends_on_file_id
JOIN pull_requests pr ON pr.id = prf.pr_id
JOIN pr_reviewers pr_rev ON pr_rev.pr_id = pr.id
JOIN developers dev ON dev.id = pr_rev.developer_id
GROUP BY dev.id, dev.name
ORDER BY review_count DESC LIMIT 3;
```

### The Cypher Graph Advantage
In **CognoDB (Cypher)**, relationships are stored as direct pointers (index-free adjacency). Traversing `(Developer)-[:REVIEWED]->(PR)-[:CHANGES]->(File)-[:DEPENDS_ON]->(File)` takes **milliseconds** regardless of dataset size:

```cypher
// Parameterized Cypher: Fast, expressive, and scalable graph pattern
MATCH (f:File {path: $filePath})<-[:CHANGES]-(pr:PullRequest)<-[r:REVIEWED]-(dev:Developer)
RETURN dev.name, count(pr) AS reviewCount, avg(r.score) AS avgScore
ORDER BY reviewCount DESC, avgScore DESC LIMIT 3;
```

---

## 📊 4. Conceptual Data Model

```mermaid
graph TD
    Dev[Developer<br/><i>name, team, tenure</i>]
    Repo[Repository<br/><i>name, language</i>]
    PR[PullRequest<br/><i>prNumber, title, status</i>]
    File[File<br/><i>path, extension, linesOfCode</i>]
    Tag[Tag<br/><i>name, category</i>]

    Dev -->|CREATED| PR
    Dev -->|REVIEWED {score}| PR
    PR -->|CHANGES {additions}| File
    File -->|DEPENDS_ON {type}| File
    PR -->|TAGGED_WITH| Tag
    Dev -->|FOLLOWS| Dev
```

---

## 🚀 5. Setup & Execution Guide

### Prerequisites
- Java 21 LTS & Maven
- Node.js (v18+) & npm
- A free **CognoDB Cloud** database instance

### 1. Configure Environment Variables
```bash
export NEO4J_URI="bolt+s://<instance-id>.databases.cognodb.cloud"
export NEO4J_USERNAME="cognodb"
export NEO4J_PASSWORD="<your-generated-password>"
```

### 2. Seed CognoDB Database
Copy and execute [`seed/seed.cypher`](file:///D:/devpulse/seed/seed.cypher) in the CognoDB Web Console or Cypher Shell.

### 3. Run Backend (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run
```
The REST API will start on `http://localhost:8080`.

### 4. Run Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173` to interact with the DevPulse Dashboard.

---

## 📁 Repository Structure

```
DevPulse/
├── backend/                  # Spring Boot 3 Java 21 Backend
│   ├── src/main/java/com/chaitu/devpulse/
│   │   ├── config/           # CORS Config
│   │   ├── controller/       # REST Endpoints (/api/experts, /api/reviewers, /api/radar, /api/health)
│   │   ├── dto/              # API DTOs (ExpertFinderDto, ReviewerRouterDto, HealthRadarDto)
│   │   ├── model/            # SDN Node Entities (@Node("Developer"), etc.)
│   │   └── service/          # DevPulseGraphService (Parameterized Cypher execution)
│   └── pom.xml               # Maven configuration
├── frontend/                 # React 18 + Vite Web Application
│   ├── src/
│   │   ├── App.jsx           # Complete DevPulse Engineering Intelligence Dashboard UI
│   │   └── index.css         # Styling system
│   └── vite.config.js        # API proxy config
├── seed/                     # Seed Scripts
│   └── seed.cypher           # Cypher dataset (Developers, PRs, Files, Dependencies)
└── README.md                 # Complete Assignment Documentation
```
