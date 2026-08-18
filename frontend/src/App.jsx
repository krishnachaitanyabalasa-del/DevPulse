import React, { useState, useEffect } from 'react';
import { 
  LayoutDashboard, 
  Search, 
  GitPullRequest, 
  ShieldAlert, 
  Share2, 
  FolderGit2, 
  Users, 
  Tag, 
  FileText, 
  Settings, 
  Bell, 
  HelpCircle, 
  LogOut, 
  ChevronRight, 
  Sparkles,
  ArrowRight,
  Maximize2,
  ZoomIn,
  ZoomOut,
  Lock,
  Calendar,
  CheckCircle2,
  Info
} from 'lucide-react';

export default function App() {
  const [activeTab, setActiveTab] = useState('Overview');
  const [healthStatus, setHealthStatus] = useState(null);

  // Expert Finder State
  const [expertSearch, setExpertSearch] = useState('OrderService.java');
  const [expertData, setExpertData] = useState(null);

  // Reviewer Router State
  const [selectedPr, setSelectedPr] = useState('PR #102 feat: integrate payment gateway');
  const [reviewerData, setReviewerData] = useState(null);

  // Health Radar State
  const [radarData, setRadarData] = useState(null);

  // Fetch Health & Real APIs
  useEffect(() => {
    fetchHealth();
    fetchExpertData('OrderService.java');
    fetchReviewerData('OrderService.java');
    fetchRadarData();
  }, []);

  const fetchHealth = async () => {
    try {
      const res = await fetch('/api/health');
      if (res.ok) setHealthStatus(await res.json());
    } catch (e) {
      setHealthStatus({ connected: false });
    }
  };

  const fetchExpertData = async (query) => {
    try {
      const res = await fetch(`/api/experts?query=${encodeURIComponent(query)}`);
      if (res.ok) setExpertData(await res.json());
    } catch (e) {
      console.error('Failed to fetch expert data', e);
    }
  };

  const fetchReviewerData = async (file) => {
    try {
      const res = await fetch(`/api/reviewers/recommend?file=${encodeURIComponent(file)}`);
      if (res.ok) setReviewerData(await res.json());
    } catch (e) {
      console.error('Failed to fetch reviewer recommendation', e);
    }
  };

  const fetchRadarData = async () => {
    try {
      const res = await fetch('/api/radar/bus-factor');
      if (res.ok) setRadarData(await res.json());
    } catch (e) {
      console.error('Failed to fetch radar data', e);
    }
  };

  return (
    <div className="app-layout">
      {/* Sidebar */}
      <aside className="sidebar">
        <div>
          <div className="sidebar-header">
            <div className="sidebar-logo">
              <Share2 size={20} />
            </div>
            <div>
              <div className="sidebar-brand-name">DevPulse</div>
              <div className="sidebar-brand-sub">Engineering Intelligence</div>
            </div>
          </div>

          <ul className="nav-menu">
            {[
              { id: 'Overview', label: 'Overview', icon: LayoutDashboard },
              { id: 'Expert Finder', label: 'Expert Finder', icon: Search },
              { id: 'Reviewer Router', label: 'Reviewer Router', icon: GitPullRequest },
              { id: 'Health Radar', label: 'Health Radar', icon: ShieldAlert },
              { id: 'Graph Explorer', label: 'Graph Explorer', icon: Share2 },
              { id: 'Repositories', label: 'Repositories', icon: FolderGit2 },
              { id: 'Developers', label: 'Developers', icon: Users },
              { id: 'Tags', label: 'Tags', icon: Tag },
              { id: 'Reports', label: 'Reports', icon: FileText },
              { id: 'Settings', label: 'Settings', icon: Settings },
            ].map((item) => {
              const Icon = item.icon;
              return (
                <li key={item.id}>
                  <button 
                    className={`nav-item-btn ${activeTab === item.id ? 'active' : ''}`}
                    onClick={() => setActiveTab(item.id)}
                  >
                    <Icon size={18} />
                    {item.label}
                  </button>
                </li>
              );
            })}
          </ul>
        </div>

        {/* Bottom User Card */}
        <div>
          <div className="user-profile-card">
            <img 
              src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80" 
              alt="John Doe" 
              className="user-avatar"
            />
            <div>
              <div className="user-name">John Doe</div>
              <div className="user-role">Backend Engineer</div>
            </div>
          </div>
        </div>
      </aside>

      {/* Main Area */}
      <main className="main-content">
        {/* Top Header */}
        <header className="top-header">
          <div className="global-search">
            <Search size={16} className="search-icon" />
            <input 
              type="text" 
              placeholder="Search files, developers, repositories, tags..." 
              value={expertSearch}
              onChange={(e) => setExpertSearch(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && fetchExpertData(expertSearch)}
            />
            <span className="kbd-shortcut">⌘ K</span>
          </div>

          <div className="top-controls">
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

        {/* Welcome Section */}
        <section className="welcome-section">
          <h1 className="welcome-title">Welcome back, John! 👋</h1>
          <p className="welcome-subtitle">Here's what's happening in your engineering graph.</p>
        </section>

        {/* Metric Cards Grid */}
        <section className="stats-grid">
          <div className="stat-card">
            <div>
              <div className="stat-title"><Users size={16} color="var(--accent-blue)" /> Developers</div>
              <div className="stat-value">52</div>
              <div className="stat-trend">▲ 8% vs last 30 days</div>
            </div>
            {/* Sparkline SVG */}
            <svg width="60" height="30" viewBox="0 0 60 30" fill="none">
              <path d="M2 25 L15 20 L30 24 L45 10 L58 4" stroke="#2563eb" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>

          <div className="stat-card">
            <div>
              <div className="stat-title"><FolderGit2 size={16} color="var(--accent-purple)" /> Repositories</div>
              <div className="stat-value">12</div>
              <div className="stat-trend">▲ 4% vs last 30 days</div>
            </div>
            <svg width="60" height="30" viewBox="0 0 60 30" fill="none">
              <path d="M2 22 L18 25 L32 15 L46 18 L58 6" stroke="#8b5cf6" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>

          <div className="stat-card">
            <div>
              <div className="stat-title"><GitPullRequest size={16} color="var(--accent-emerald)" /> Pull Requests</div>
              <div className="stat-value">328</div>
              <div className="stat-trend">▲ 12% vs last 30 days</div>
            </div>
            <svg width="60" height="30" viewBox="0 0 60 30" fill="none">
              <path d="M2 28 L15 18 L30 22 L45 8 L58 12" stroke="#10b981" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>

          <div className="stat-card">
            <div>
              <div className="stat-title"><FileText size={16} color="var(--accent-amber)" /> Files</div>
              <div className="stat-value">4,732</div>
              <div className="stat-trend">▲ 7% vs last 30 days</div>
            </div>
            <svg width="60" height="30" viewBox="0 0 60 30" fill="none">
              <path d="M2 20 L16 12 L30 25 L44 14 L58 6" stroke="#f59e0b" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>
        </section>

        {/* Dashboard 2-Column Grid */}
        <div className="dashboard-grid">
          {/* Card 1: Graph Overview */}
          <div className="card">
            <div className="card-header">
              <div className="card-title">
                Graph Overview <Info size={14} className="info-icon" />
              </div>
              <button className="btn-view-profile" style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                Explore Graph <Maximize2 size={12} />
              </button>
            </div>

            {/* Force-directed Graph Representation */}
            <div className="graph-canvas-container">
              {/* Floating controls */}
              <div style={{ position: 'absolute', left: 12, top: 12, display: 'flex', flexDirection: 'column', gap: 4, zIndex: 10 }}>
                <button className="icon-btn" style={{ width: 28, height: 28 }}><ZoomIn size={14} /></button>
                <button className="icon-btn" style={{ width: 28, height: 28 }}><ZoomOut size={14} /></button>
                <button className="icon-btn" style={{ width: 28, height: 28 }}><Maximize2 size={14} /></button>
                <button className="icon-btn" style={{ width: 28, height: 28 }}><Lock size={14} /></button>
              </div>

              <svg width="100%" height="100%" viewBox="0 0 500 240">
                {/* Edges */}
                <line x1="250" y1="120" x2="160" y2="80" stroke="#cbd5e1" strokeWidth="1.5" />
                <line x1="250" y1="120" x2="340" y2="80" stroke="#cbd5e1" strokeWidth="1.5" />
                <line x1="250" y1="120" x2="180" y2="170" stroke="#cbd5e1" strokeWidth="1.5" />
                <line x1="250" y1="120" x2="320" y2="170" stroke="#cbd5e1" strokeWidth="1.5" />
                <line x1="160" y1="80" x2="100" y2="60" stroke="#cbd5e1" strokeWidth="1.5" />
                <line x1="340" y1="80" x2="410" y2="60" stroke="#cbd5e1" strokeWidth="1.5" />

                {/* Center Node (File/Repo) */}
                <circle cx="250" cy="120" r="22" fill="#8b5cf6" opacity="0.9" />
                <rect x="242" y="112" width="16" height="16" fill="white" rx="2" />

                {/* Surrounding Nodes */}
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

            {/* Legend */}
            <div className="graph-legend">
              <div className="legend-item"><div className="legend-dot" style={{ background: '#2563eb' }} /> Developer</div>
              <div className="legend-item"><div className="legend-dot" style={{ background: '#8b5cf6' }} /> Repository</div>
              <div className="legend-item"><div className="legend-dot" style={{ background: '#10b981' }} /> Pull Request</div>
              <div className="legend-item"><div className="legend-dot" style={{ background: '#f59e0b' }} /> File</div>
              <div className="legend-item"><div className="legend-dot" style={{ background: '#94a3b8' }} /> Tag</div>
            </div>
          </div>

          {/* Card 2: Smart Reviewer Router */}
          <div className="card">
            <div className="card-header">
              <div className="card-title">
                Smart Reviewer Router <Info size={14} className="info-icon" />
              </div>
            </div>

            {/* Selector */}
            <div style={{ marginBottom: '14px' }}>
              <select 
                style={{ width: '100%', padding: '8px 12px', borderRadius: '8px', border: '1px solid var(--border-color)', background: 'white', fontWeight: 600, fontSize: '0.85rem' }}
                value={selectedPr}
                onChange={(e) => { setSelectedPr(e.target.value); fetchReviewerData(e.target.value); }}
              >
                <option>PR #102 feat: integrate payment gateway</option>
                <option>PR #45 refactor OrderService</option>
                <option>PR #88 harden AuthCore JWT</option>
              </select>
            </div>

            <div style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-muted)', marginBottom: '8px' }}>
              Top recommended reviewers
            </div>

            <div className="reviewer-list">
              {[
                { rank: 1, name: 'Emily Carter', avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80', score: 95, color: '#10b981', reason: 'Reviewed 4 files in this module' },
                { rank: 2, name: 'Mark Johnson', avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80', score: 78, color: '#2563eb', reason: 'Reviewed dependent files' },
                { rank: 3, name: 'Sarah Williams', avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80', score: 62, color: '#f59e0b', reason: 'Connected via 2 hops' },
              ].map((rec) => (
                <div key={rec.rank} className="reviewer-item">
                  <div className="reviewer-info">
                    <span className="reviewer-rank">{rec.rank}</span>
                    <img src={rec.avatar} alt={rec.name} className="reviewer-avatar" />
                    <div>
                      <div className="reviewer-name">{rec.name}</div>
                      <div className="reviewer-reason">{rec.reason}</div>
                    </div>
                  </div>

                  <div style={{ textAlign: 'right' }}>
                    <span style={{ fontWeight: 800, fontSize: '0.85rem', color: rec.color }}>{rec.score}%</span>
                    <div className="reviewer-progress-bar">
                      <div className="progress-fill" style={{ width: `${rec.score}%`, background: rec.color }} />
                    </div>
                  </div>

                  <button className="btn-view-profile">View Profile</button>
                </div>
              ))}
            </div>

            <a className="footer-link" onClick={() => setActiveTab('Reviewer Router')}>
              View all recommended reviewers →
            </a>
          </div>
        </div>

        {/* Dashboard Lower 2-Column Grid */}
        <div className="dashboard-grid">
          {/* Card 3: Expert Finder */}
          <div className="card">
            <div className="card-header">
              <div className="card-title">
                Expert Finder <Info size={14} className="info-icon" />
              </div>
            </div>

            <div style={{ display: 'flex', gap: '8px', marginBottom: '14px' }}>
              <input 
                type="text" 
                style={{ flex: 1, padding: '8px 14px', borderRadius: '8px', border: '1px solid var(--border-color)', fontSize: '0.85rem' }}
                placeholder="Search file path or tech (e.g., PaymentService.java, JWT, Kubernetes)"
                value={expertSearch}
                onChange={(e) => setExpertSearch(e.target.value)}
              />
              <button 
                style={{ padding: '8px 18px', background: '#2563eb', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 600, cursor: 'pointer' }}
                onClick={() => fetchExpertData(expertSearch)}
              >
                Search
              </button>
            </div>

            <div style={{ background: '#f1f5f9', padding: '6px 12px', borderRadius: '6px', fontSize: '0.8rem', fontFamily: 'var(--font-mono)', display: 'inline-block', marginBottom: '12px' }}>
              📄 src/main/java/com/payments/PaymentGateway.java
            </div>

            {/* 3-Hop Path Visualizer */}
            <div className="path-node-chain">
              <div className="path-step-card">
                <div className="path-step-icon" style={{ background: '#fef3c7', color: '#d97706' }}><FileText size={20} /></div>
                <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>PaymentGateway.java</span>
                <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>File</span>
              </div>

              <span className="path-edge-label">CHANGES</span>

              <div className="path-step-card">
                <div className="path-step-icon" style={{ background: '#d1fae5', color: '#10b981' }}><GitPullRequest size={20} /></div>
                <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>PR #88</span>
                <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>Pull Request</span>
              </div>

              <span className="path-edge-label">REVIEWED BY</span>

              <div className="path-step-card">
                <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80" alt="Emily" className="path-step-icon" />
                <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>Emily Carter</span>
                <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>Reviewer</span>
              </div>

              <span className="path-edge-label">FOLLOWS</span>

              <div className="path-step-card">
                <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80" alt="John" className="path-step-icon" />
                <span style={{ fontSize: '0.75rem', fontWeight: 700 }}>John Doe (You)</span>
                <span style={{ fontSize: '0.68rem', color: '#94a3b8' }}>You</span>
              </div>
            </div>

            <div style={{ fontSize: '0.8rem', color: '#475569', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <CheckCircle2 size={14} color="#10b981" />
              <span>Emily has reviewed 4 files in this module and is <strong>online 🟢</strong></span>
            </div>

            <a className="footer-link" onClick={() => setActiveTab('Expert Finder')}>
              View full path in graph →
            </a>
          </div>

          {/* Card 4: Health Radar */}
          <div className="card">
            <div className="card-header">
              <div className="card-title">
                Health Radar <Info size={14} className="info-icon" />
              </div>
              <select style={{ padding: '4px 8px', borderRadius: '6px', border: '1px solid var(--border-color)', fontSize: '0.78rem' }}>
                <option>Risk Score</option>
                <option>Centrality</option>
              </select>
            </div>

            <table className="radar-table">
              <thead>
                <tr>
                  <th>File</th>
                  <th>Centrality Score</th>
                  <th>Developer Density</th>
                  <th>Risk Score</th>
                  <th>Risk Level</th>
                </tr>
              </thead>
              <tbody>
                {[
                  { path: 'src/.../PaymentGateway.java', centrality: '0.82', density: '0.18', score: '0.91', level: 'High', badge: 'badge-high' },
                  { path: 'src/.../AuthService.java', centrality: '0.76', density: '0.22', score: '0.78', level: 'High', badge: 'badge-high' },
                  { path: 'src/.../OrderService.java', centrality: '0.65', density: '0.35', score: '0.60', level: 'Medium', badge: 'badge-medium' },
                  { path: 'src/.../UserController.java', centrality: '0.45', density: '0.62', score: '0.32', level: 'Low', badge: 'badge-low' },
                  { path: 'src/.../EmailService.java', centrality: '0.28', density: '0.71', score: '0.18', level: 'Low', badge: 'badge-low' },
                ].map((row, idx) => (
                  <tr key={idx}>
                    <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600 }}>{row.path}</td>
                    <td>{row.centrality}</td>
                    <td>{row.density}</td>
                    <td style={{ fontWeight: 700 }}>{row.score}</td>
                    <td>
                      <span className={`badge-risk ${row.badge}`}>{row.level}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            <a className="footer-link" onClick={() => setActiveTab('Health Radar')}>
              View full health report →
            </a>
          </div>
        </div>
      </main>
    </div>
  );
}
