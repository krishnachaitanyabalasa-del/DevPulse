import React from 'react';
import { FileText, GitPullRequest } from 'lucide-react';

export default function ExpertFinder({ query, setQuery, onSearch, result }) {
  const experts = result?.experts || [];

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">Expert Finder (3-Hop Graph Traversal)</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '20px' }}>
        Traverses graph connections to find engineers with deep contextual knowledge of files, PRs, and dependencies.
      </p>

      <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
        <input 
          type="text" 
          style={{ flex: 1, padding: '10px 16px', borderRadius: '8px', border: '1px solid var(--border-color)', fontSize: '0.9rem' }}
          placeholder="Search file path or tag (e.g., OrderService.java, PaymentGateway.java)"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && onSearch(query)}
        />
        <button 
          style={{ padding: '10px 24px', background: '#2563eb', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 700, cursor: 'pointer' }}
          onClick={() => onSearch(query)}
        >
          Search Graph
        </button>
      </div>

      {result?.cypherQuery && (
        <div style={{ background: '#0f172a', color: '#38bdf8', padding: '12px 16px', borderRadius: '8px', fontFamily: 'var(--font-mono)', fontSize: '0.78rem', marginBottom: '20px', overflowX: 'auto' }}>
          <strong>Executed Parameterized Cypher Query:</strong><br/>
          {result.cypherQuery}
        </div>
      )}

      {/* 3-Hop Traversal Chain */}
      {experts.length > 0 ? (
        experts.map((exp, idx) => (
          <div key={idx} className="path-node-chain" style={{ padding: '24px', marginBottom: '16px' }}>
            <div className="path-step-card">
              <div className="path-step-icon" style={{ background: '#fef3c7', color: '#d97706', width: 48, height: 48 }}>
                <FileText size={22} />
              </div>
              <span style={{ fontSize: '0.85rem', fontWeight: 700 }}>{result?.targetFile?.path || query}</span>
              <span style={{ fontSize: '0.72rem', color: '#94a3b8' }}>Target File</span>
            </div>

            <span className="path-edge-label">CHANGES</span>

            <div className="path-step-card">
              <div className="path-step-icon" style={{ background: '#d1fae5', color: '#10b981', width: 48, height: 48 }}>
                <GitPullRequest size={22} />
              </div>
              <span style={{ fontSize: '0.85rem', fontWeight: 700 }}>PR #{88 + idx * 14}</span>
              <span style={{ fontSize: '0.72rem', color: '#94a3b8' }}>Pull Request</span>
            </div>

            <span className="path-edge-label">REVIEWED BY</span>

            <div className="path-step-card">
              <img 
                src={exp.developer.avatarUrl || "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80"} 
                alt={exp.developer.name} 
                className="path-step-icon" 
                style={{ width: 48, height: 48 }}
              />
              <span style={{ fontSize: '0.85rem', fontWeight: 700 }}>{exp.developer.name}</span>
              <span style={{ fontSize: '0.72rem', color: '#94a3b8' }}>{exp.developer.team}</span>
            </div>

            <span className="path-edge-label">FOLLOWS</span>

            <div className="path-step-card">
              <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80" alt="John" className="path-step-icon" style={{ width: 48, height: 48 }} />
              <span style={{ fontSize: '0.85rem', fontWeight: 700 }}>John Doe (You)</span>
              <span style={{ fontSize: '0.72rem', color: '#94a3b8' }}>You</span>
            </div>
          </div>
        ))
      ) : (
        <div style={{ padding: '30px', textAlign: 'center', color: 'var(--text-muted)' }}>
          No expert traversal path found for "{query}" in backend database.
        </div>
      )}
    </div>
  );
}
