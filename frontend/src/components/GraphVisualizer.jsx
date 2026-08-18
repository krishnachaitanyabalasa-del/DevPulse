import React from 'react';
import { ZoomIn, ZoomOut, Maximize2, Lock } from 'lucide-react';

export default function GraphVisualizer() {
  return (
    <div className="card">
      <div className="card-header">
        <div className="card-title">
          Graph Overview
        </div>
        <button className="btn-view-profile" style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
          Explore Graph <Maximize2 size={12} />
        </button>
      </div>

      <div className="graph-canvas-container">
        <div style={{ position: 'absolute', left: 12, top: 12, display: 'flex', flexDirection: 'column', gap: 4, zIndex: 10 }}>
          <button className="icon-btn" style={{ width: 28, height: 28 }}><ZoomIn size={14} /></button>
          <button className="icon-btn" style={{ width: 28, height: 28 }}><ZoomOut size={14} /></button>
          <button className="icon-btn" style={{ width: 28, height: 28 }}><Maximize2 size={14} /></button>
          <button className="icon-btn" style={{ width: 28, height: 28 }}><Lock size={14} /></button>
        </div>

        <svg width="100%" height="100%" viewBox="0 0 500 240">
          <line x1="250" y1="120" x2="160" y2="80" stroke="#cbd5e1" strokeWidth="1.5" />
          <line x1="250" y1="120" x2="340" y2="80" stroke="#cbd5e1" strokeWidth="1.5" />
          <line x1="250" y1="120" x2="180" y2="170" stroke="#cbd5e1" strokeWidth="1.5" />
          <line x1="250" y1="120" x2="320" y2="170" stroke="#cbd5e1" strokeWidth="1.5" />
          <line x1="160" y1="80" x2="100" y2="60" stroke="#cbd5e1" strokeWidth="1.5" />
          <line x1="340" y1="80" x2="410" y2="60" stroke="#cbd5e1" strokeWidth="1.5" />

          <circle cx="250" cy="120" r="22" fill="#8b5cf6" opacity="0.9" />
          <rect x="242" y="112" width="16" height="16" fill="white" rx="2" />

          <circle cx="160" cy="80" r="14" fill="#2563eb" />
          <circle cx="340" cy="80" r="14" fill="#10b981" />
          <circle cx="180" cy="170" r="14" fill="#f59e0b" />
          <circle cx="320" cy="170" r="14" fill="#2563eb" />

          <circle cx="100" cy="60" r="10" fill="#94a3b8" />
          <circle cx="410" cy="60" r="10" fill="#94a3b8" />
          <circle cx="80" cy="140" r="12" fill="#2563eb" />
          <circle cx="420" cy="140" r="12" fill="#f59e0b" />
        </svg>
      </div>

      <div className="graph-legend">
        <div className="legend-item"><div className="legend-dot" style={{ background: '#2563eb' }} /> Developer</div>
        <div className="legend-item"><div className="legend-dot" style={{ background: '#8b5cf6' }} /> Repository</div>
        <div className="legend-item"><div className="legend-dot" style={{ background: '#10b981' }} /> Pull Request</div>
        <div className="legend-item"><div className="legend-dot" style={{ background: '#f59e0b' }} /> File</div>
        <div className="legend-item"><div className="legend-dot" style={{ background: '#94a3b8' }} /> Tag</div>
      </div>
    </div>
  );
}
