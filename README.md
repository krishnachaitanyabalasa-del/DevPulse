# DevPulse - Engineering Knowledge & Code Review Intelligence

> Find the right engineer. Route the right reviewer. Detect knowledge risk.

DevPulse is a graph-powered engineering intelligence application for software teams. It models relationships between developers, repositories, pull requests, files, tags, code dependencies, and review history to answer questions that are difficult to express with traditional relational data models.

The application is backed by CognoDB Cloud, using openCypher and the Neo4j-compatible Java ecosystem through a Spring Boot backend.

---

## Demo & Repository Information

- Hosted Backend API: https://devpulse-qpvz.onrender.com
- API Health Endpoint: https://devpulse-qpvz.onrender.com/api/health
- GitHub Repository: https://github.com/krishnachaitanyabalasa-del/DevPulse
- Assignment Submission Target: hr@wexa.ai

---

## Why DevPulse?

Engineering teams accumulate knowledge in source code, pull requests, reviews, and developer collaboration.

Traditional project-management and source-control tools can answer questions such as:
- What tickets are open?
- Which pull requests exist?
- Who committed recently?

But they do not naturally answer relationship-heavy questions such as:
- Who understands this file?
- Who has reviewed code related to this module?
- Who is the best reviewer for this change?
- Which files are depended on by many other modules?
- Which critical modules have very few developers with review history?

DevPulse represents these connections directly as a graph.

### Core Features

| Feature | Purpose |
| :--- | :--- |
| Expert Finder | Finds developers connected to a file through pull requests and review history |
| Smart Reviewer Router | Ranks developers by their relevance and historical review quality to a file or PR |
| Health Radar | Detects files with high dependency centrality and low developer coverage |
| Graph Explorer | Provides a visual way to inspect connected engineering entities and paths |

---

# Why a Graph Database?

DevPulse is fundamentally a relationship-oriented application.

For example, finding an expert for `OrderService.java` requires traversing:

```
Developer
    ↓ REVIEWED
Pull Request
    ↓ CHANGES
File
```

Understanding the impact of changing a file can require traversing:

```
File A
   ↓ DEPENDS_ON
File B
   ↓ DEPENDS_ON
File C
```

In a relational database, these questions require several join tables and potentially recursive queries. As relationship depth grows, the queries become increasingly cumbersome and slow.

With CognoDB, the relationships are represented directly in the graph and queried using Cypher patterns.

Example:

```cypher
MATCH (f:File)
WHERE toLower(f.path) CONTAINS toLower($file)
MATCH (pr:PullRequest)-[:CHANGES]->(f)
MATCH (dev:Developer)-[r:REVIEWED]->(pr)
RETURN dev, count(pr) AS reviewCount, avg(r.score) AS averageScore
ORDER BY reviewCount DESC
```

This directly expresses:

```
File ← CHANGES ← PullRequest ← REVIEWED ← Developer
```

That makes graph traversal a natural fit for DevPulse.

---

# Architecture

```
┌─────────────────────────────────────────────┐
│                React Frontend               │
│                                             │
│ Overview  Expert Finder  Reviewer Router    │
│ Health Radar  Repositories  Developers      │
└──────────────────────┬──────────────────────┘
                       │ REST / JSON
                       ▼
┌─────────────────────────────────────────────┐
│          Spring Boot 3.2.5 Backend          │
│                 Java 21                     │
│                                             │
│ Controllers → Services → Neo4jClient        │
└──────────────────────┬──────────────────────┘
                       │ Bolt / openCypher
                       ▼
┌─────────────────────────────────────────────┐
│              CognoDB Cloud                  │
│                                             │
│ Developer • Repository • PR • File • Tag   │
│        + typed relationships                │
└─────────────────────────────────────────────┘
```

---

# Technology Stack

### Backend
- Java 21
- Spring Boot 3.2.5
- Spring Data Neo4j (`Neo4jClient`)
- REST APIs
- Parameterized Cypher queries

### Database
- CognoDB Cloud
- openCypher
- Bolt 5.0–5.4 protocol

### Frontend
- React 18
- Vite
- Modular Page CSS / Responsive UI

### Deployment
- Backend: Render (Docker Container Service)
- Database: CognoDB Cloud

---

# Graph Data Model

## Nodes

### Developer
```text
Developer
├── id
├── name
├── team
├── tenure
└── avatarUrl
```

### Repository
```text
Repository
├── id
├── name
└── language
```

### PullRequest
```text
PullRequest
├── id
├── prNumber
├── title
├── status
└── createdAt
```

### File
```text
File
├── id
├── path
├── extension
└── linesOfCode
```

### Tag
```text
Tag
├── id
└── name
```

---

## Relationships

```text
Developer ──CREATED───────► PullRequest
Developer ──REVIEWED──────► PullRequest
Developer ──MAINTAINS─────► File

PullRequest ──CHANGES─────► File
PullRequest ──TAGGED_WITH─► Tag

File ──DEPENDS_ON─────────► File
File ──HAS_TAG────────────► Tag

Developer ──CONTRIBUTES_TO► Repository
Repository ──CONTAINS─────► File
```

### Relationship Properties

`REVIEWED`:
```text
score
thoroughness
```

`CHANGES`:
```text
additions
```

`DEPENDS_ON`:
```text
type
```

These properties allow DevPulse to use more than simple connectivity when calculating relevance and risk.

---

# Graph Model Diagram

```text
                         ┌─────────────┐
                         │    Tag      │
                         └──────▲──────┘
                                │ HAS_TAG / TAGGED_WITH
                                │
┌─────────────┐   CREATED   ┌───┴─────────┐
│  Developer  ├────────────►│ PullRequest │
└──────┬──────┘             └─────┬───────┘
       │                          │
       │ REVIEWED                 │ CHANGES
       │                          │
       │                          ▼
       │                    ┌───────────┐
       │                    │   File    │
       │                    └─────┬─────┘
       │                          │
       │ MAINTAINS                │ DEPENDS_ON
       ▼                          ▼
┌─────────────┐             ┌───────────┐
│ Repository  │             │   File    │
└──────┬──────┘             └───────────┘
       │ CONTAINS
       ▼
    ┌──────┐
    │ File │
    └──────┘
```

---

# Seed Data

The repository contains a Cypher seed script:

```text
backend/src/main/resources/seed.cypher
```

The current seed creates realistic engineering data including:
- 10 developers
- 5 repositories
- 12 files
- 6 tags
- 8 pull requests
- File dependency relationships
- Pull-request file modifications
- Pull-request tags
- Developer authorship & maintainership
- Developer review history

The seed includes weighted review relationships such as:

```cypher
(d:Developer)-[:REVIEWED {
    score: 95,
    thoroughness: "HIGH"
}]->(pr:PullRequest)
```

and dependency metadata such as:

```cypher
(a:File)-[:DEPENDS_ON {
    type: "IMPORT"
}]->(b:File)
```

This provides enough connected data to demonstrate multi-hop graph queries and risk analysis.

---

# Main Application Flows

## 1. Expert Finder

### User Problem
> "I have a bug in `OrderService.java`. Who understands this code?"

The user searches for a file.

DevPulse traverses:
```text
File ← CHANGES ← PullRequest ← REVIEWED ← Developer
```

The result contains Developer, Team, Tenure, Review count, and relationship path explanation.

---

## 2. Smart Reviewer Router

### User Problem
> "Who should review this change?"

The Reviewer Router evaluates developers using their relationship to the requested file and its surrounding dependency hierarchy.

Example:
```text
1. Sarah Jenkins     95% Reviewer Fit
2. Alex Rivera       78% Reviewer Fit
3. Carlos Mendez     62% Reviewer Fit
```

The score is a relevance score derived from graph evidence such as maintainership, review history, and dependency proximity.

---

## 3. Health Radar

### User Problem
> "Which parts of our codebase are dangerous because too few people understand them?"

Health Radar analyzes the graph around files. A module becomes higher risk when many other files depend on it while few developers have reviewed changes involving it.

Example:
```text
PaymentGateway.java

Dependency in-degree: 3
Developer density:    0.33
Risk level:           HIGH (Bus Factor Risk)
```

---

# Expanded Cypher Query Suite

DevPulse leverages openCypher queries executing over Bolt 5.0. Below are the key graph algorithms and traversals powering the application:

### Query 1: Expert Finder (Multi-Hop Traversal)

Finds engineers connected to a target file through historical Pull Requests and Code Reviews:

```cypher
MATCH (f:File)
WHERE toLower(f.path) CONTAINS toLower($q) OR f.id = $q
MATCH (pr:PullRequest)-[:CHANGES]->(f)
MATCH (dev:Developer)-[r:REVIEWED]->(pr)
RETURN
    f.id AS fileId,
    f.path AS filePath,
    f.extension AS extension,
    f.linesOfCode AS linesOfCode,
    dev.id AS developerId,
    dev.name AS developerName,
    dev.team AS team,
    dev.tenure AS tenure,
    count(pr) AS reviewCount,
    avg(r.score) AS averageReviewScore
ORDER BY reviewCount DESC, averageReviewScore DESC
LIMIT 5
```

---

### Query 2: Smart Reviewer Router Recommendation

Ranks candidate reviewers using weighted scores from file maintainership and historical PR reviews:

```cypher
MATCH (f:File)
WHERE f.path = $filePath OR f.id = $filePath
MATCH (d:Developer)
OPTIONAL MATCH (pr:PullRequest)-[:CHANGES]->(f)
OPTIONAL MATCH (d)-[r:REVIEWED]->(pr)
OPTIONAL MATCH (d)-[m:MAINTAINS]->(f)
WITH d, count(r) AS reviewCount, count(m) AS isMaintainer
WHERE reviewCount > 0 OR isMaintainer > 0
RETURN d.id AS id, 
       d.name AS name, 
       d.team AS team, 
       d.tenure AS tenure, 
       d.avatarUrl AS avatarUrl,
       (reviewCount * 25.0 + isMaintainer * 50.0) AS relevanceScore
ORDER BY relevanceScore DESC
LIMIT 5
```

---

### Query 3: Health Radar (Bus Factor & Dependency Centrality)

Calculates module risk by analyzing incoming dependency in-degree against unique reviewer density:

```cypher
MATCH (target:File)
OPTIONAL MATCH (dep:File)-[:DEPENDS_ON]->(target)
OPTIONAL MATCH (pr:PullRequest)-[:CHANGES]->(target)
OPTIONAL MATCH (rev:Developer)-[:REVIEWED]->(pr)
WITH target, 
     count(DISTINCT dep) AS inDegree, 
     count(DISTINCT rev) AS revCount, 
     collect(DISTINCT rev) AS reviewers
RETURN target.id AS f_id, 
       target.path AS f_path, 
       target.extension AS f_ext, 
       target.linesOfCode AS f_loc, 
       inDegree, 
       revCount, 
       reviewers
ORDER BY inDegree DESC
```

---

### Query 4: Transitive Dependency Chain & Risk Propagation (2 to 4 Hops)

Discovers upstream files affected when a core module is refactored across multi-hop dependency chains:

```cypher
MATCH path = (source:File {path: $filePath})<-[:DEPENDS_ON*1..4]-(dependent:File)
RETURN dependent.path AS dependentFile,
       dependent.linesOfCode AS loc,
       length(path) AS dependencyDepth,
       [node IN nodes(path) | node.path] AS propagationChain
ORDER BY dependencyDepth ASC
```

---

### Query 5: Cross-Team Knowledge Transfer & Collaboration Traversal

Identifies cross-team developers who review PRs outside their primary repository ownership:

```cypher
MATCH (d:Developer)-[:MAINTAINS]->(homeRepo:Repository)
MATCH (d)-[:REVIEWED]->(pr:PullRequest)-[:CHANGES]->(f:File)
MATCH (otherRepo:Repository)-[:CONTAINS]->(f)
WHERE homeRepo <> otherRepo
RETURN d.name AS developerName,
       d.team AS homeTeam,
       homeRepo.name AS homeRepository,
       otherRepo.name AS reviewedExternalRepository,
       count(pr) AS externalReviewsCount
ORDER BY externalReviewsCount DESC
```

---

### Query 6: High Complexity & Low-Coverage Hotspot Detection

Detects large files (>300 lines of code) with high dependency connections that lack sufficient active maintainers:

```cypher
MATCH (f:File)
WHERE f.linesOfCode > 300
OPTIONAL MATCH (dep:File)-[:DEPENDS_ON]->(f)
OPTIONAL MATCH (m:Developer)-[:MAINTAINS]->(f)
WITH f, count(dep) AS inwardDependencies, count(m) AS maintainerCount
WHERE maintainerCount <= 1 AND inwardDependencies >= 1
RETURN f.path AS vulnerableFile,
       f.linesOfCode AS loc,
       inwardDependencies,
       maintainerCount,
       "High Knowledge Risk" AS riskCategory
ORDER BY inwardDependencies DESC
```

---

# REST API Reference

| Endpoint | Method | Purpose |
| :--- | :--- | :--- |
| `/api/health` | GET | Returns database connectivity status, node count, and relationship count |
| `/api/seed` | GET/POST | Triggers transactional graph database seeding |
| `/api/experts` | GET/POST | Traverses graph for 3-hop file expert search (`?query=OrderService.java`) |
| `/api/experts/developers` | GET | Returns list of all Developer nodes |
| `/api/reviewers/recommend` | GET/POST | Ranks recommended PR reviewers for a specified file |
| `/api/radar/bus-factor` | GET | Returns Bus Factor risk assessment across all files |
| `/api/radar/files` | GET | Returns list of all File nodes |
| `/api/repositories` | GET | Returns list of all Repository nodes |

---

# Parameterized Queries in Java

All user-provided values are safely passed to Cypher as parameters using `Neo4jClient` to prevent Cypher injection:

```java
neo4jClient.query("""
    MATCH (f:File)
    WHERE toLower(f.path) CONTAINS toLower($q)
    RETURN f
""")
.bind(query)
.to("q");
```

---

# Project Structure

```text
devpulse/
├── backend/
│   ├── src/main/java/com/chaitu/devpulse/
│   │   ├── controller/DevPulseController.java     # REST API Controller
│   │   ├── dto/                                  # Data Transfer Objects
│   │   ├── model/                                # Graph Node Models
│   │   └── service/                              # Cypher Graph Services
│   │       ├── DeveloperService.java
│   │       ├── ExpertFinderService.java
│   │       ├── FileService.java
│   │       ├── GraphHealthService.java
│   │       ├── PullRequestService.java
│   │       ├── RadarService.java
│   │       ├── RepositoryService.java
│   │       ├── ReviewerRouterService.java
│   │       └── SeedService.java                  # Transactional Seeding
│   ├── src/main/resources/
│   │   ├── application.properties               # Neo4j Driver Connection Config
│   │   └── seed.cypher                           # openCypher Seed Script
│   ├── Dockerfile                                # Multi-Stage Build Spec
│   └── pom.xml                                   # Spring Boot 3.2.5 (Java 21)
├── frontend/
│   ├── src/
│   │   ├── components/                           # Header, Sidebar, MetricCard
│   │   ├── pages/                                # Overview, Experts, Reviewers, Radar
│   │   ├── styles/                               # Dedicated Page CSS Modules
│   │   └── App.jsx                               # Router & Global Layout
│   └── package.json                              # Vite + React Dependencies
├── docker-compose.yml                            # Local Stack Spec
└── render.yaml                                   # Render Cloud Infrastructure Spec
```

---

# Local Setup

## 1. Clone Repository
```bash
git clone https://github.com/krishnachaitanyabalasa-del/DevPulse.git
cd DevPulse
```

## 2. Create CognoDB Cloud Instance
1. Go to https://console.cognodb.com/signup and create a free account.
2. Provision a free `c0` instance in your preferred region.
3. Save the generated Bolt Connection URI (`bolt+s://<instance-id>.databases.cognodb.com:7687`), username (`cognodb`), and password.

## 3. Configure Environment Variables
```bash
export NEO4J_URI="bolt+s://<your-instance-id>.databases.cognodb.com:7687"
export NEO4J_USERNAME="cognodb"
export NEO4J_PASSWORD="<your-password>"
```

## 4. Run Backend
```bash
cd backend
./mvnw spring-boot:run
```

## 5. Run Frontend
```bash
cd frontend
npm install
npm run dev
```

---

# Security & Resilience

- Database secrets are read strictly from environment variables.
- Queries use parameterized binding (`bind().to()`).
- Database exceptions are caught gracefully by `GraphHealthService` to prevent application downtime.

---



# Author

**Krishna Chaitanya Balasa**  
Computer Science Engineering  
VIT-AP University  
Submission Contact: `hr@wexa.ai`
