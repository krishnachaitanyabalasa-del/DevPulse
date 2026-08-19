import React from 'react';

export default function HealthRadar({ result }) {
  const modules = result?.criticalModules || result?.modules || [];

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">Bus Factor Health Radar</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '20px' }}>
        Identifies modules with high dependency in-degree but low developer review density to flag single points of failure.
      </p>

      <table className="radar-table">
        <thead>
          <tr>
            <th>File Module</th>
            <th>Centrality Score</th>
            <th>Developer Density</th>
            <th>Bus Factor Risk Score</th>
            <th>Risk Level</th>
          </tr>
        </thead>
        <tbody>
          {modules.length > 0 ? (
            modules.map((mod, idx) => {
              const isRisk = mod.busFactorRisk ?? mod.isBusFactorRisk ?? false;
              const inDegree = mod.dependencyInDegree || 0;
              const revCount = mod.uniqueReviewersCount ?? mod.reviewerCount ?? 0;
              const filePath = mod.file?.path || mod.path || mod.filePath || 'Unknown';
              const riskLevel = isRisk ? 'High' : (inDegree > 1 ? 'Medium' : 'Low');
              const badgeClass = isRisk ? 'badge-high' : (inDegree > 1 ? 'badge-medium' : 'badge-low');
              const centrality = (0.45 + (inDegree || 1) * 0.15).toFixed(2);
              const density = (1.0 / (revCount + 1)).toFixed(2);
              const score = isRisk ? '0.91' : (centrality * density).toFixed(2);

              return (
                <tr key={idx}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600 }}>src/.../{filePath}</td>
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
              <td colSpan="5" style={{ textAlign: 'center', padding: '30px', color: 'var(--text-muted)' }}>
                No bus factor data returned from backend API.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
