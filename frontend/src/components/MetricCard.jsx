import React from 'react';

export default function MetricCard({ 
  title, 
  value, 
  badge = "↑ 8%", 
  subtitle = "Live CognoDB Graph Nodes", 
  icon: Icon, 
  iconBg = "#eff6ff", 
  color = "#2563eb", 
  sparklinePath 
}) {
  return (
    <div 
      style={{
        background: '#ffffff',
        borderRadius: '16px',
        padding: '24px',
        border: '1px solid #f1f5f9',
        boxShadow: '0 4px 20px -2px rgba(0, 0, 0, 0.03)',
        display: 'flex',
        flexDirection: 'column',
        justify: 'space-between',
        position: 'relative',
        overflow: 'hidden',
        minHeight: '190px'
      }}
    >
      {/* Top Section: Icon & Title */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
        <div 
          style={{
            width: 40,
            height: 40,
            borderRadius: '10px',
            background: iconBg,
            display: 'flex',
            alignItems: 'center',
            justify: 'center'
          }}
        >
          <Icon size={20} color={color} />
        </div>
        <span style={{ fontSize: '0.95rem', fontWeight: 700, color: '#0f172a' }}>{title}</span>
      </div>

      {/* Middle Section: Big Value & Percentage Badge */}
      <div style={{ display: 'flex', alignItems: 'baseline', gap: '12px', marginBottom: '16px' }}>
        <span style={{ fontSize: '2.4rem', fontWeight: 800, color: '#0f172a', lineHeight: 1 }}>{value}</span>
        <span 
          style={{
            background: '#d1fae5',
            color: '#047857',
            padding: '3px 8px',
            borderRadius: '12px',
            fontSize: '0.75rem',
            fontWeight: 700,
            display: 'inline-flex',
            alignItems: 'center',
            gap: '2px'
          }}
        >
          {badge}
        </span>
      </div>

      {/* Sparkline Wave Chart */}
      <div style={{ width: '100%', height: '36px', marginBottom: '8px' }}>
        <svg width="100%" height="100%" viewBox="0 0 200 40" preserveAspectRatio="none" fill="none">
          <path 
            d={sparklinePath || "M0 30 Q 30 10, 60 25 T 120 15 T 180 20 L 200 5"} 
            stroke={color} 
            strokeWidth="2.5" 
            strokeLinecap="round" 
            fill="none" 
          />
        </svg>
      </div>

      {/* Subtitle */}
      <div style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 500 }}>
        {subtitle}
      </div>
    </div>
  );
}
