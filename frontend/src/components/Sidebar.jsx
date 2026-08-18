import React from 'react';
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
  Settings 
} from 'lucide-react';

export default function Sidebar({ activeTab, setActiveTab }) {
  const menuItems = [
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
  ];

  return (
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
          {menuItems.map((item) => {
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
  );
}
