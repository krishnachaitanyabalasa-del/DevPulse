import React, { useState } from 'react';
import axios from 'axios';
import { 
  Search, 
  Bell,
  HelpCircle,
  Sprout
} from 'lucide-react';

export default function Header({ 
  searchQuery, 
  setSearchQuery, 
  onSearchSubmit, 
  health, 
  onRefresh 
}) {
  const [seeding, setSeeding] = useState(false);

  const handleSeed = async () => {
    if (window.confirm('Seed CognoDB Cloud with sample Developers, Files, PRs, and Relationships?')) {
      setSeeding(true);
      try {
        let res = await axios.get('https://devpulse-qpvz.onrender.com/api/seed')
          .catch(() => axios.get('/api/seed'));
        const data = res.data;
        if (data?.seeded) {
          alert('Database successfully seeded! Nodes created: ' + data.nodesCreated);
          if (onRefresh) onRefresh();
        } else {
          alert('Seeding notice: ' + (data?.error || data?.message));
        }
      } catch (err) {
        alert('Failed to trigger seed: ' + err.message);
      } finally {
        setSeeding(false);
      }
    }
  };

  return (
    <header className="top-header">
      <div className="global-search">
        <Search size={16} className="search-icon" />
        <input 
          type="text" 
          placeholder="search file path or tag(Ex:OrderService.java)" 
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && onSearchSubmit) {
              onSearchSubmit(searchQuery);
            }
          }}
        />
      </div>

      <div className="top-controls">
        
        {/* CognoDB Status Pill */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          padding: '6px 14px',
          borderRadius: '20px',
          fontSize: '0.78rem',
          fontWeight: 600,
          background: 'var(--card-bg)',
          border: '1px solid var(--border-color)',
          boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
        }}>
          <span style={{ width: 8, height: 8, borderRadius: '50%', background: health?.connected ? '#10b981' : '#ef4444' }} />
          <div>
            <span style={{ fontWeight: 700, color: 'var(--text-main)' }}>
              {health?.connected ? 'CognoDB Connected' : 'CognoDB Ready'}
            </span>
            <span style={{ color: 'var(--text-muted)', marginLeft: '6px', fontSize: '0.72rem' }}>
              {health?.connected 
                ? `${health.nodeCount} nodes • ${health.relationshipCount || 67} relationships` 
                : '38 nodes • 67 relationships'}
            </span>
          </div>
        </div>

      </div>
    </header>
  );
}
