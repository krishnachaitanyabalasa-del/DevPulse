import React from 'react';
import GraphVisualizer from '../components/GraphVisualizer';

export default function GraphExplorer() {
  return (
    <div>
      <div style={{ marginBottom: '20px' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 800 }}>Graph Explorer</h2>
        <p style={{ color: 'var(--text-muted)' }}>Interactive topology graph map of developers, PRs, files, and dependencies.</p>
      </div>

      <GraphVisualizer />
    </div>
  );
}
