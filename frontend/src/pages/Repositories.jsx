import React, { useState } from 'react';
import { FolderGit2, Search, Code2, GitBranch, FileCode, CheckCircle2 } from 'lucide-react';
import '../styles/Repositories.css';

export default function Repositories({ repositories = [], files = [] }) {
  const [filterQuery, setFilterQuery] = useState('');

  const filteredRepos = (repositories || []).filter(repo =>
    (repo.name && repo.name.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (repo.language && repo.language.toLowerCase().includes(filterQuery.toLowerCase())) ||
    (repo.id && repo.id.toLowerCase().includes(filterQuery.toLowerCase()))
  );

  return (
    <div className="card repositories-container">
      <div className="card-header">
        <h2 className="card-title">Repositories</h2>
      </div>

      <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '8px' }}>
        Live codebase repositories, architectural languages, and indexed graph nodes from backend API.
      </p>

      {/* Filter / Search Bar */}
      <div style={{ display: 'flex', gap: '12px', marginBottom: '12px' }}>
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

      {/* Repository Cards Grid with Repositories.css auto-layout */}
      {filteredRepos.length > 0 ? (
        <div className="repositories-grid">
          {filteredRepos.map((repo, idx) => {
            const associatedFiles = files.slice(idx * 2, idx * 2 + 3);

            return (
              <div key={repo.id || idx} className="repo-card">
                <div>
                  <div className="repo-card-header">
                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <div className="repo-icon-box">
                        <FolderGit2 size={20} />
                      </div>
                      <div>
                        <h3 className="repo-title">{repo.name}</h3>
                        <div className="repo-meta">
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

                    <span className="repo-status-badge">
                      Active
                    </span>
                  </div>

                  <div className="repo-files-section">
                    <div className="repo-files-title">
                      INDEXED FILE MODULES ({files.length} Total)
                    </div>
                    <div className="repo-files-list">
                      {associatedFiles.length > 0 ? (
                        associatedFiles.map((f, i) => (
                          <span key={i} className="repo-file-chip">
                            <FileCode size={11} color="#64748b" /> {f.path}
                          </span>
                        ))
                      ) : (
                        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Connected to Graph</span>
                      )}
                    </div>
                  </div>
                </div>

                
              </div>
            );
          })}
        </div>
      ) : (
        <div style={{ padding: '30px', textAlign: 'center', color: 'var(--text-muted)' }}>
          {repositories.length === 0 
            ? 'No repositories returned from backend API. Seed your database or click "Seed DB" to load repositories.'
            : `No repositories match the filter "${filterQuery}".`}
        </div>
      )}
    </div>
  );
}
