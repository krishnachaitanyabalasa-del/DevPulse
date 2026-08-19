import React, { useState } from 'react';
import { Search, UserCheck } from 'lucide-react';

export default function Developers({ developers = [] }) {
  const [filterQuery, setFilterQuery] = useState('');

  const filteredDevs = developers.filter(dev =>
    (dev.name && dev.name.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (dev.team && dev.team.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (dev.tenure && dev.tenure.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (dev.id && dev.id.toLowerCase().includes(filterQuery.toLowerCase()))
  );

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">Developers</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '20px' }}>
        Live engineering team members fetched directly from backend `/api/experts/developers`.
      </p>

      {/* Filter / Search Bar */}
      <div style={{ display: 'flex', gap: '12px', marginBottom: '24px' }}>
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

      {/* Developers Grid */}
      {filteredDevs.length > 0 ? (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '16px' }}>
          {filteredDevs.map((dev, idx) => (
            <div
              key={dev.id || idx}
              style={{
                border: '1px solid var(--border-color)',
                borderRadius: 'var(--radius-md)',
                padding: '20px',
                background: '#ffffff',
                display: 'flex',
                flexDirection: 'column',
                justify: 'space-between'
              }}
            >
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '14px' }}>
                  <img
                    src={dev.avatarUrl || 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80'}
                    alt={dev.name}
                    style={{
                      width: 52,
                      height: 52,
                      borderRadius: '50%',
                      objectFit: 'cover',
                      border: '2px solid #e2e8f0'
                    }}
                  />
                  <div>
                    <h3 style={{ fontSize: '1.05rem', fontWeight: 700, color: 'var(--text-main)' }}>{dev.name}</h3>
                    <div style={{ fontSize: '0.82rem', fontWeight: 600, color: '#2563eb' }}>{dev.team || 'Engineering'}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{dev.tenure || 'Team Member'}</div>
                  </div>
                </div>

                <div
                  style={{
                    background: '#f8fafc',
                    borderRadius: '8px',
                    padding: '10px 12px',
                    marginBottom: '16px',
                    display: 'flex',
                    justify: 'space-around',
                    textAlign: 'center',
                    fontSize: '0.78rem'
                  }}
                >
                  <div>
                    <div style={{ fontWeight: 800, fontSize: '0.95rem', color: 'var(--text-main)' }}>ID</div>
                    <div style={{ color: 'var(--text-muted)' }}>{dev.id}</div>
                  </div>
                  <div style={{ borderLeft: '1px solid #e2e8f0' }} />
                  <div>
                    <div style={{ fontWeight: 800, fontSize: '0.95rem', color: '#10b981' }}>Active</div>
                    <div style={{ color: 'var(--text-muted)' }}>Status</div>
                  </div>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '8px' }}>
                <button
                  className="btn-view-profile"
                  style={{
                    flex: 1,
                    padding: '8px 12px',
                    borderRadius: '8px',
                    fontSize: '0.8rem',
                    fontWeight: 600,
                    display: 'flex',
                    alignItems: 'center',
                    justify: 'center',
                    gap: '6px'
                  }}
                >
                  <UserCheck size={14} /> Assign Review
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div style={{ padding: '30px', textAlign: 'center', color: 'var(--text-muted)' }}>
          No developers returned from backend API. Seed your CognoDB database to display developers.
        </div>
      )}
    </div>
  );
}
