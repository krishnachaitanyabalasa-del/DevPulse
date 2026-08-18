import React, { useState } from 'react';
import { 
  Search, 
  Database, 
  RefreshCw, 
  Calendar, 
  Bell, 
  HelpCircle, 
  LogOut,
  Sprout
} from 'lucide-react';

export default function Header({ 
  searchQuery, 
  setSearchQuery, 
  onSearchSubmit, 
  health, 
  loading, 
  onRefresh 
}) {
  const [seeding, setSeeding] = useState(false);

  const handleSeed = async () => {
    if (window.confirm('Seed CognoDB Cloud with sample Developers, Files, PRs, and Relationships?')) {
      setSeeding(true);
      try {
        let res = await fetch('/api/seed');
        if (!res.ok) res = await fetch('http://localhost:8080/api/seed');
        const data = await res.json();
        if (data.seeded) {
          alert('🌱 Database successfully seeded! Nodes created: ' + data.nodesCreated);
          if (onRefresh) onRefresh();
        } else {
          alert('Seeding notice: ' + (data.error || data.message));
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
          placeholder="Search files, developers, repositories, tags... (Press Enter)" 
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && onSearchSubmit) {
              onSearchSubmit(searchQuery);
            }
          }}
        />
        <span className="kbd-shortcut">⌘ K</span>
      </div>

      <div className="top-controls">
        <button 
          onClick={handleSeed}
          disabled={seeding}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '6px',
            padding: '6px 14px',
            borderRadius: '20px',
            fontSize: '0.78rem',
            fontWeight: 700,
            background: '#2563eb',
            color: 'white',
            border: 'none',
            cursor: 'pointer'
          }}
        >
          <Sprout size={14} className={seeding ? 'spin' : ''} />
          {seeding ? 'Seeding DB...' : 'Seed DB'}
        </button>

        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          padding: '6px 12px',
          borderRadius: '20px',
          fontSize: '0.78rem',
          fontWeight: 600,
          background: health?.connected ? '#d1fae5' : '#fee2e2',
          color: health?.connected ? '#047857' : '#b91c1c'
        }}>
          <Database size={14} />
          {health?.connected ? `CognoDB Connected (${health.nodeCount} nodes)` : 'Backend Sync Ready'}
        </div>

        <button className="icon-btn" onClick={onRefresh} title="Refresh API Data">
          <RefreshCw size={16} className={loading ? 'spin' : ''} />
        </button>

        <div className="date-range-picker">
          <Calendar size={16} color="var(--text-muted)" />
          <span>May 6 – Jun 5, 2025</span>
        </div>
        
        <button className="icon-btn">
          <Bell size={18} />
          <div className="notification-badge" />
        </button>
        <button className="icon-btn">
          <HelpCircle size={18} />
        </button>
        <button className="icon-btn">
          <LogOut size={18} />
        </button>
      </div>
    </header>
  );
}
