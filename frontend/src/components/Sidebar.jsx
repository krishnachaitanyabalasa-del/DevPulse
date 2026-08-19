import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Search, 
  GitPullRequest, 
  ShieldAlert, 
  FolderGit2, 
  Users, 
  Moon,
  Sun
} from 'lucide-react';
import logoImg from '../images/logo.png';

export default function Sidebar({ darkMode, setDarkMode }) {
  const menuItems = [
    { label: 'Overview', icon: LayoutDashboard, path: '/' },
    { label: 'Expert Finder', icon: Search, path: '/experts' },
    { label: 'Reviewer Router', icon: GitPullRequest, path: '/reviewers' },
    { label: 'Health Radar', icon: ShieldAlert, path: '/radar' },
    { label: 'Repositories', icon: FolderGit2, path: '/repositories' },
    { label: 'Developers', icon: Users, path: '/developers' },
  ];

  return (
    <aside className="sidebar">
      <div>
        {/* Sidebar Header with logo.png */}
        <div className="sidebar-header" style={{ padding: '8px 12px 24px', display: 'flex', alignItems: 'center', gap: '10px' }}>
          <img 
            src={logoImg} 
            alt="DevPulse Logo" 
            style={{ height: '36px', width: 'auto', objectFit: 'contain' }} 
          />
          <div>
            <div className="sidebar-brand-name" style={{ fontSize: '1.2rem', fontWeight: 800 }}>DevPulse</div>
            <div className="sidebar-brand-sub" style={{ fontSize: '0.7rem', color: '#64748b' }}>Engineering Intelligence</div>
          </div>
        </div>

        {/* Navigation Menu */}
        <ul className="nav-menu">
          {menuItems.map((item) => {
            const Icon = item.icon;
            return (
              <li key={item.path}>
                <NavLink 
                  to={item.path}
                  end={item.path === '/'}
                  className={({ isActive }) => `nav-item-btn ${isActive ? 'active' : ''}`}
                >
                  <Icon size={18} />
                  {item.label}
                </NavLink>
              </li>
            );
          })}
        </ul>
      </div>

      {/* Dark Mode & Bright Mode Toggle (Profile card removed as requested) */}
      <div style={{ marginTop: 'auto', paddingTop: '20px' }}>
        <div 
          onClick={() => setDarkMode(!darkMode)}
          style={{
            display: 'flex',
            alignItems: 'center',
            justify: 'space-between',
            padding: '12px 14px',
            background: darkMode ? 'rgba(255, 255, 255, 0.08)' : 'rgba(255, 255, 255, 0.05)',
            borderRadius: '12px',
            color: darkMode ? '#f8fafc' : '#94a3b8',
            fontSize: '0.85rem',
            fontWeight: 600,
            cursor: 'pointer',
            border: '1px solid rgba(255, 255, 255, 0.1)',
            userSelect: 'none'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            {darkMode ? <Sun size={18} color="#f59e0b" /> : <Moon size={18} />}
            <span>{darkMode ? 'Bright Mode' : 'Dark Mode'}</span>
          </div>

          <div 
            style={{
              width: 38,
              height: 22,
              borderRadius: 11,
              background: darkMode ? '#3b82f6' : '#475569',
              position: 'relative',
              transition: 'background 0.2s'
            }}
          >
            <div 
              style={{
                width: 18,
                height: 18,
                borderRadius: '50%',
                background: 'white',
                position: 'absolute',
                top: 2,
                left: darkMode ? 18 : 2,
                transition: 'left 0.2s ease',
                boxShadow: '0 1px 3px rgba(0,0,0,0.3)'
              }}
            />
          </div>
        </div>
      </div>
    </aside>
  );
}
