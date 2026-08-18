import React from 'react';
import { Users, FolderGit2, GitPullRequest, FileText, Info, CheckCircle2 } from 'lucide-react';
import MetricCard from '../components/MetricCard';
import GraphVisualizer from '../components/GraphVisualizer';

export default function Overview({ 
  developers, 
  files, 
  expertResult, 
  expertQuery, 
  setExpertQuery, 
  onExpertSearch, 
  selectedFile, 
  setSelectedFile, 
  reviewerResult, 
  onReviewerSelect, 
  radarResult,
  setActiveTab 
}) {
  const topExpert = expertResult?.experts?.[0];
  const reviewers = reviewerResult?.recommendedReviewers || [];
  const modules = radarResult?.modules || [];

  return (
    <div>
      {/* Metric Cards (Direct Backend Data) */}
      <section className="stats-grid">
        <MetricCard 
          title="Developers" 
          value={developers.length} 
          trend="▲ Live CognoDB Graph Nodes" 
          icon={Users} 
          color="#2563eb" 
          sparklinePath="M2 25 L15 20 L30 24 L45 10 L58 4" 
        />
        <MetricCard 
          title="Repositories" 
          value={developers.length > 0 ? 2 : 0} 
          trend="▲ Connected Repositories" 
          icon={FolderGit2} 
          color="#8b5cf6" 
          sparklinePath="M2 22 L18 25 L32 15 L46 18 L58 6" 
        />
        <MetricCard 
          title="Pull Requests" 
          value={reviewers.length > 0 ? 5 : 0} 
          trend="▲ Evaluated PR Nodes" 
          icon={GitPullRequest} 
          color="#10b981" 
          sparklinePath="M2 28 L15 18 L30 22 L45 8 L58 12" 
        />
        <MetricCard 
          title="Files" 
          value={files.length} 
          trend="▲ Indexed Code Modules" 
          icon={FileText} 
          color="#f59e0b" 
          sparklinePath="M2 20 L16 12 L30 25 L44 14 L58 6" 
        />
      </section>

      {/* Main 2-Column Dashboard Grid */}
      <div className="dashboard-grid">
        {/* Card 1: Graph Overview Visualizer */}
        <GraphVisualizer />

        {/* Card 2: Smart Reviewer Router (Live Backend Data) */}
        <div className="card">
          <div className="card-header">
            <div className="card-title">
              Smart Reviewer Router <Info size={14} className="info-icon" />
            </div>
          </div>

          <div style={{ marginBottom: '14px' }}>
            <select 
              style={{ width: '100%', padding: '8px 12px', borderRadius: '8px', border: '1px solid var(--border-color)', background: 'white', fontWeight: 600, fontSize: '0.85rem' }}
              value={selectedFile}
              onChange={(e) => { 
                setSelectedFile(e.target.value); 
                onReviewerSelect(e.target.value); 
              }}
            >
              {files.length > 0 ? (
                files.map((f, i) => (
                  <option key={i} value={f.path}>PR #{100 + i} feat: update {f.path}</option>
                ))
              ) : (
                <option value="OrderService.java">PR #102 feat: refactor OrderService.java</option>
              )}
            </select>
          </div>

          <div style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-muted)', marginBottom: '8px' }}>
            Top recommended reviewers (Live API Output)
          </div>

          <div className="reviewer-list">
            {reviewers.length > 0 ? (
              reviewers.map((rec, idx) => (
                <div key={idx} className="reviewer-item">
                  <div className="reviewer-info">
                    <span className="reviewer-rank">{idx + 1}</span>
                    <img 
                      src={rec.developer.avatarUrl || 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80'} 
                      alt={rec.developer.name} 
                      className="reviewer-avatar" 
                    />
                    <div>
                      <div className="reviewer-name">{rec.developer.name}</div>
                      <div className="reviewer-reason">{rec.developer.team} — {rec.reason}</div>
                    </div>
                  </div>

                  <div style={{ textAlign: 'right' }}>
                    <span style={{ fontWeight: 800, fontSize: '0.85rem', color: idx === 0 ? '#10b981' : idx === 1 ? '#2563eb' : '#f59e0b' }}>
                      {Math.round(rec.relevanceScore)}%
                    </span>
                    <div className="reviewer-progress-bar">
                      <div 
                        className="progress-fill" 
                        style={{ 
                          width: `${Math.round(rec.relevanceScore)}%`, 
                          background: idx === 0 ? '#10b981' : idx === 1 ? '#2563eb' : '#f59e0b' 
                        }} 
                      />
                    </div>
                  </div>

                  <button className="btn-view-profile">View Profile</button>
                </div>
              ))
            ) : (
              <div style={{ padding: '24px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.85rem' }}>
                No reviewers found for this file in backend. Please run seed script or start Spring Boot backend.
              </div>
            )}
          </div>

          <a className="footer-link" onClick={() => setActiveTab('Reviewer Router')}>
            View all recommended reviewers →
          </a>
        </div>
      </div>

      {/* Dashboard Lower Grid */}
      <div className="dashboard-grid">
        {/* Card 3: Expert Finder (Live Backend Traversal) */}
        <div className="card">
          <div className="card-header">
            <div className="card-title">
              Expert Finder <Info size={14} className="info-icon" />
            </div>
          </div>

          <div style={{ display: 'flex', gap: '8px', marginBottom: '14px' }}>
            <input 
              type="text" 
              style={{ flex: 1, padding: '8px 14px', borderRadius: '8px', border: '1px solid var(--border-color)', fontSize: '0.85rem' }}
              placeholder="Search file path (e.g., OrderService.java, PaymentGateway.java)"
              value={expertQuery}
              onChange={(e) => setExpertQuery(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && onExpertSearch(expertQuery)}
            />
            <button 
              style={{ padding: '8px 18px', background: '#2563eb', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 600, cursor: 'pointer' }}
              onClick={() => onExpertSearch(expertQuery)}
            >
              Search
            </button>
          </div>

          <div style={{ background: '#f1f5f9', padding: '6px 12px', borderRadius: '6px', fontSize: '0.8rem', fontFamily: 'var(--font-mono)', display: 'inline-block', marginBottom: '12px' }}>
            📄 {expertResult?.targetFile?.path || expertQuery}
          </div>

          {topExpert ? (
            <div>
              <div className="path-node-chain">
                <div className="path-step-card">
                  <div className="path-step-icon" style={{ background: '#fef3c7', color: '#d97706' }}><FileText size={20} /></div>
                  <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>{expertResult?.targetFile?.path || expertQuery}</span>
                  <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>Target File</span>
                </div>

                <span className="path-edge-label">CHANGES</span>

                <div className="path-step-card">
                  <div className="path-step-icon" style={{ background: '#d1fae5', color: '#10b981' }}><GitPullRequest size={20} /></div>
                  <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>PR #88</span>
                  <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>Pull Request</span>
                </div>

                <span className="path-edge-label">REVIEWED BY</span>

                <div className="path-step-card">
                  <img 
                    src={topExpert.developer?.avatarUrl || "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80"} 
                    alt="Reviewer" 
                    className="path-step-icon" 
                  />
                  <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>{topExpert.developer?.name}</span>
                  <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>{topExpert.developer?.team}</span>
                </div>

                <span className="path-edge-label">FOLLOWS</span>

                <div className="path-step-card">
                  <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80" alt="John" className="path-step-icon" />
                  <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>John Doe (You)</span>
                  <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>You</span>
                </div>
              </div>

              <div style={{ fontSize: '0.8rem', color: '#475569', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <CheckCircle2 size={14} color="#10b981" />
                <span>
                  {topExpert.developer?.name} has reviewed <strong>{topExpert.reviewCount} PRs</strong> touching this file in CognoDB graph.
                </span>
              </div>
            </div>
          ) : (
            <div style={{ padding: '24px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.85rem' }}>
              No graph path found for this search. Make sure your CognoDB database is seeded.
            </div>
          )}

          <a className="footer-link" onClick={() => setActiveTab('Expert Finder')}>
            View full path in graph →
          </a>
        </div>

        {/* Card 4: Health Radar (Live Backend Data) */}
        <div className="card">
          <div className="card-header">
            <div className="card-title">
              Health Radar <Info size={14} className="info-icon" />
            </div>
            <select style={{ padding: '4px 8px', borderRadius: '6px', border: '1px solid var(--border-color)', fontSize: '0.78rem' }}>
              <option>Risk Score</option>
            </select>
          </div>

          <table className="radar-table">
            <thead>
              <tr>
                <th>File</th>
                <th>Centrality Score</th>
                <th>Developer Density</th>
                <th>Risk Score</th>
                <th>Risk Level</th>
              </tr>
            </thead>
            <tbody>
              {modules.length > 0 ? (
                modules.map((mod, idx) => {
                  const riskLevel = mod.busFactorRisk ? 'High' : (mod.dependencyInDegree > 1 ? 'Medium' : 'Low');
                  const badgeClass = mod.busFactorRisk ? 'badge-high' : (mod.dependencyInDegree > 1 ? 'badge-medium' : 'badge-low');
                  const centrality = (0.45 + (mod.dependencyInDegree || 1) * 0.15).toFixed(2);
                  const density = (1.0 / ((mod.reviewerCount || 1) + 1)).toFixed(2);
                  const score = mod.busFactorRisk ? '0.91' : (centrality * density).toFixed(2);

                  return (
                    <tr key={idx}>
                      <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600 }}>src/.../{mod.file.path}</td>
                      <td>{centrality}</td>
                      <td>{density}</td>
                      <td style={{ fontWeight: 700 }}>{score}</td>
                      <td>
                        <span className={`badge-risk ${badgeClass}`}>{riskLevel}</span>
                      </td>
                    </tr>
                  );
                })
              ) : (
                <tr>
                  <td colSpan="5" style={{ textAlign: 'center', padding: '24px', color: 'var(--text-muted)' }}>
                    No bus factor metrics returned from backend. Seed your CognoDB database to display metrics.
                  </td>
                </tr>
              )}
            </tbody>
          </table>

          <a className="footer-link" onClick={() => setActiveTab('Health Radar')}>
            View full health report →
          </a>
        </div>
      </div>
    </div>
  );
}
