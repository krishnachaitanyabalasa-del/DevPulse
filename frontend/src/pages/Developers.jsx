import React, { useState } from 'react';
import { Search, UserCheck } from 'lucide-react';
import '../styles/Developers.css';

export default function Developers({ developers = [] }) {
  const [filterQuery, setFilterQuery] = useState('');

  const filteredDevs = (developers || []).filter(dev =>
    (dev.name && dev.name.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (dev.team && dev.team.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (dev.tenure && dev.tenure.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (dev.id && dev.id.toLowerCase().includes(filterQuery.toLowerCase()))
  );

  return (
    <div className="card developers-container">
      <div className="card-header">
        <h2 className="card-title">Developers</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '8px' }}>
        Live engineering team members fetched directly from backend `/api/experts/developers`.
      </p>

      {/* Filter / Search Bar */}
      <div style={{ display: 'flex', gap: '12px', marginBottom: '12px' }}>
        <div className="global-search" style={{ flex: 1, maxWidth: '100%' }}>
          <Search size={16} className="search-icon" />
          <input
            type="text"
            placeholder="Search developer name, team, or role..."
            value={filterQuery}
            onChange={(e) => setFilterQuery(e.target.value)}
          />
        </div>
      </div>

      {/* Developers Grid with Developers.css auto-layout */}
      {filteredDevs.length > 0 ? (
        <div className="developers-grid">
          {filteredDevs.map((dev, idx) => (
            <div key={dev.id || idx} className="dev-card">
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '10px' }}>
                  <img
                    src={dev.avatarUrl || 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80'}
                    alt={dev.name}
                    className="dev-avatar"
                  />
                  <div>
                    <h3 className="dev-name">{dev.name}</h3>
                    <div className="dev-team">{dev.team || 'Engineering'}</div>
                    <div className="dev-tenure">{dev.tenure || 'Team Member'}</div>
                  </div>
                </div>

                <div className="dev-stats-box">
                  <div>
                    <div className="dev-stats-title">ID</div>
                    <div style={{ color: 'var(--text-muted)' }}>{dev.id}</div>
                  </div>
                  <div style={{ borderLeft: '1px solid var(--border-color)' }} />
                  <div>
                    <div className="dev-stats-title" style={{ color: '#10b981' }}>Active</div>
                    <div style={{ color: 'var(--text-muted)' }}>Status</div>
                  </div>
                </div>
              </div>

              
            </div>
          ))}
        </div>
      ) : (
        <div style={{ padding: '30px', textAlign: 'center', color: 'var(--text-muted)' }}>
          {developers.length === 0 
            ? 'No developers returned from backend API. Seed your database or click "Seed DB" to load developers.'
            : `No developers match the filter "${filterQuery}".`}
        </div>
      )}
    </div>
  );
}
