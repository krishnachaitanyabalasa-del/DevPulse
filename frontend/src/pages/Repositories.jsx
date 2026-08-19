import React, { useState } from 'react';
import { FolderGit2, Search, Code2, GitBranch, FileCode, CheckCircle2 } from 'lucide-react';

export default function Repositories({ repositories = [], files = [] }) {
  const [filterQuery, setFilterQuery] = useState('');

  const filteredRepos = repositories.filter(repo =>
    (repo.name && repo.name.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (repo.language && repo.language.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (repo.id && repo.id.toLowerCase().includes(filterQuery.toLowerCase()))
  );

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">Repositories</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '20px' }}>
        Live codebase repositories, architectural languages, and indexed graph nodes from backend API.
      </p>

      {/* Filter / Search Bar */}
      <div style={{ display: 'flex', gap: '12px', marginBottom: '24px' }}>
        <div className="global-search" style={{ flex: 1, maxWidth: '100%' }}>
          <Search size={16} className="search-icon" />
          <input
            type="text"
            placeholder="Search repository name or language..."
            value={filterQuery}
            onChange={(e) => setFilterQuery(e.target.value)}
          />
        </div>
      </div>

      {/* Repository Cards Grid */}
      {filteredRepos.length > 0 ? (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '16px' }}>
          {filteredRepos.map((repo, idx) => {
            // Associated files for this repo from backend files list
            const associatedFiles = files.slice(idx * 2, idx * 2 + 3);

            return (
              <div
                key={repo.id || idx}
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
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                      <div
                        style={{
                          width: 38,
                          height: 38,
                          borderRadius: '8px',
                          background: '#eff6ff',
                          color: '#2563eb',
                          display: 'flex',
                          alignItems: 'center',
                          justify: 'center'
                        }}
                      >
                        <FolderGit2 size={20} />
                      </div>
                      <div>
                        <h3 style={{ fontSize: '1rem', fontWeight: 700, color: 'var(--text-main)' }}>{repo.name}</h3>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.78rem', color: 'var(--text-muted)' }}>
                          <span style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                            <GitBranch size={12} /> main
                          </span>
                          <span>•</span>
                          <span style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                            <Code2 size={12} /> {repo.language || 'Java'}
                          </span>
                        </div>
                      </div>
                    </div>

                    <span
                      style={{
                        padding: '3px 8px',
                        borderRadius: '12px',
                        fontSize: '0.72rem',
                        fontWeight: 700,
                        background: '#d1fae5',
                        color: '#047857'
                      }}
                    >
                      Active
                    </span>
                  </div>

                  <div style={{ marginBottom: '16px' }}>
                    <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-subtle)', marginBottom: '6px' }}>
                      INDEXED FILE MODULES ({files.length} Total)
                    </div>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                      {associatedFiles.length > 0 ? (
                        associatedFiles.map((f, i) => (
                          <span
                            key={i}
                            style={{
                              background: '#f1f5f9',
                              color: '#334155',
                              padding: '4px 8px',
                              borderRadius: '6px',
                              fontSize: '0.75rem',
                              fontFamily: 'var(--font-mono)',
                              display: 'inline-flex',
                              alignItems: 'center',
                              gap: '4px'
                            }}
                          >
                            <FileCode size={11} color="#64748b" /> {f.path}
                          </span>
                        ))
                      ) : (
                        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Connected to Graph</span>
                      )}
                    </div>
                  </div>
                </div>

                <div
                  style={{
                    display: 'flex',
                    justify: 'space-between',
                    alignItems: 'center',
                    paddingTop: '12px',
                    borderTop: '1px solid var(--border-color)',
                    fontSize: '0.8rem',
                    color: 'var(--text-muted)'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <CheckCircle2 size={14} color="#10b981" />
                    <span>CognoDB Node: {repo.id}</span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div style={{ padding: '30px', textAlign: 'center', color: 'var(--text-muted)' }}>
          No repositories found from backend API. Seed your CognoDB database to display repositories.
        </div>
      )}
    </div>
  );
}
