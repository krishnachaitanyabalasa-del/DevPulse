import React from 'react';

export default function ReviewerRouter({ selectedFile, setSelectedFile, onSelect, result }) {
  const reviewers = result?.recommendedReviewers || [];

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">🔀 Smart Reviewer Router</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '20px' }}>
        Ranks developers by past review scores and architectural dependency proximity to recommend the best PR reviewers.
      </p>

      <div style={{ marginBottom: '20px' }}>
        <select 
          style={{ width: '100%', padding: '12px 16px', borderRadius: '8px', border: '1px solid var(--border-color)', fontWeight: 700, fontSize: '0.95rem' }}
          value={selectedFile}
          onChange={(e) => {
            setSelectedFile(e.target.value);
            onSelect(e.target.value);
          }}
        >
          <option value="OrderService.java">PR #102 feat: refactor OrderService.java</option>
          <option value="PaymentGateway.java">PR #88 feat: integrate PaymentGateway.java</option>
          <option value="AuthCore.java">PR #45 harden AuthCore JWT security</option>
          <option value="TokenValidator.java">PR #22 add TokenValidator expiration check</option>
        </select>
      </div>

      <div className="reviewer-list">
        {reviewers.length > 0 ? (
          reviewers.map((rec, idx) => (
            <div key={idx} className="reviewer-item" style={{ padding: '16px' }}>
              <div className="reviewer-info">
                <span className="reviewer-rank" style={{ fontSize: '1.1rem' }}>#{idx + 1}</span>
                <img 
                  src={rec.developer.avatarUrl || 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80'} 
                  alt={rec.developer.name} 
                  className="reviewer-avatar" 
                  style={{ width: 44, height: 44 }}
                />
                <div>
                  <div className="reviewer-name" style={{ fontSize: '1rem' }}>{rec.developer.name} ({rec.developer.team || 'Engineering'})</div>
                  <div className="reviewer-reason" style={{ fontSize: '0.85rem' }}>{rec.reason}</div>
                </div>
              </div>

              <div style={{ textAlign: 'right' }}>
                <span style={{ fontWeight: 800, fontSize: '1.1rem', color: idx === 0 ? '#10b981' : idx === 1 ? '#2563eb' : '#f59e0b' }}>
                  {Math.round(rec.relevanceScore)}% Match
                </span>
                <div className="reviewer-progress-bar" style={{ width: 140, height: 8 }}>
                  <div 
                    className="progress-fill" 
                    style={{ 
                      width: `${Math.round(rec.relevanceScore)}%`, 
                      background: idx === 0 ? '#10b981' : idx === 1 ? '#2563eb' : '#f59e0b' 
                    }} 
                  />
                </div>
              </div>

              <button className="btn-view-profile" style={{ padding: '8px 16px', fontWeight: 700 }}>Assign PR</button>
            </div>
          ))
        ) : (
          <div style={{ padding: '30px', textAlign: 'center', color: 'var(--text-muted)' }}>
            No reviewers found for this file in your CognoDB database.
          </div>
        )}
      </div>
    </div>
  );
}
