import React from 'react';

export default function MetricCard({ title, value, trend, icon: Icon, color, sparklinePath }) {
  return (
    <div className="stat-card">
      <div>
        <div className="stat-title">
          <Icon size={16} color={color} /> {title}
        </div>
        <div className="stat-value">{value}</div>
        <div className="stat-trend">{trend}</div>
      </div>
      <svg width="60" height="30" viewBox="0 0 60 30" fill="none">
        <path d={sparklinePath} stroke={color} strokeWidth="2" strokeLinecap="round" />
      </svg>
    </div>
  );
}
