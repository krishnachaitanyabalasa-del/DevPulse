import React from 'react';
import { Users, FolderGit2, GitPullRequest, FileText } from 'lucide-react';
import MetricCard from '../components/MetricCard';
import devPulseLogo from '../images/image.png';
import '../styles/Overview.css';

export default function Overview({ 
  developers = [], 
  files = [], 
  repositories = [],
  pullRequests = []
}) {
  return (
    <div className="overview-container">
      {/* Hero Section - Preserved Gradient & Constellation Background with image.png Logo */}
      <div className="overview-hero-card">
        {/* Constellation SVG Network Background */}
        <svg 
          width="100%" 
          height="100%" 
          viewBox="0 0 1000 240" 
          preserveAspectRatio="none"
          style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', pointerEvents: 'none', opacity: 0.4 }}
        >
          <defs>
            <linearGradient id="heroConstellationGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stopColor="#8b5cf6" stopOpacity="0.12" />
              <stop offset="50%" stopColor="#3b82f6" stopOpacity="0.08" />
              <stop offset="100%" stopColor="#6366f1" stopOpacity="0.15" />
            </linearGradient>
          </defs>

          <path d="M0,120 Q250,60 500,130 T1000,90 V240 H0 Z" fill="url(#heroConstellationGrad)" />

          <line x1="80" y1="90" x2="200" y2="60" stroke="#cbd5e1" strokeWidth="1" strokeDasharray="4 4" opacity="0.6" />
          <line x1="200" y1="60" x2="350" y2="110" stroke="#cbd5e1" strokeWidth="1" strokeDasharray="4 4" opacity="0.6" />
          <line x1="350" y1="110" x2="520" y2="70" stroke="#cbd5e1" strokeWidth="1" strokeDasharray="4 4" opacity="0.6" />
          <line x1="520" y1="70" x2="680" y2="140" stroke="#cbd5e1" strokeWidth="1" strokeDasharray="4 4" opacity="0.6" />
          <line x1="680" y1="140" x2="850" y2="80" stroke="#cbd5e1" strokeWidth="1" strokeDasharray="4 4" opacity="0.6" />
          <line x1="850" y1="80" x2="950" y2="130" stroke="#cbd5e1" strokeWidth="1" strokeDasharray="4 4" opacity="0.6" />

          <circle cx="80" cy="90" r="4" fill="#a855f7" opacity="0.8" />
          <circle cx="200" cy="60" r="5" fill="#3b82f6" opacity="0.8" />
          <circle cx="350" cy="110" r="4" fill="#8b5cf6" opacity="0.8" />
          <circle cx="520" cy="70" r="6" fill="#6366f1" opacity="0.9" />
          <circle cx="680" cy="140" r="4" fill="#3b82f6" opacity="0.8" />
          <circle cx="850" cy="80" r="5" fill="#a855f7" opacity="0.8" />
          <circle cx="950" cy="130" r="4" fill="#8b5cf6" opacity="0.8" />
        </svg>

        {/* Hero Image Content */}
        <div className="overview-hero-content">
          <img 
            src={devPulseLogo} 
            alt="DevPulse Engineering Intelligence" 
            className="overview-hero-logo"
          />
        </div>
      </div>

      {/* Metric Cards Grid */}
      <section className="overview-metrics-grid">
        <MetricCard 
          title="Developers" 
          value={developers.length} 
          badge="↑ 8%"
          subtitle="Live CognoDB Graph Nodes" 
          icon={Users} 
          iconBg="#eff6ff"
          color="#2563eb" 
          sparklinePath="M0 30 Q 30 15, 60 25 T 120 18 T 170 12 L 200 8" 
        />

        <MetricCard 
          title="Repositories" 
          value={repositories.length} 
          badge="↑ 0%"
          subtitle="Connected Repositories" 
          icon={FolderGit2} 
          iconBg="#f3e8ff"
          color="#8b5cf6" 
          sparklinePath="M0 25 Q 40 30, 80 15 T 140 22 T 180 10 L 200 12" 
        />

        <MetricCard 
          title="Pull Requests" 
          value={pullRequests.length} 
          badge="↑ 12%"
          subtitle="Evaluated PR Nodes" 
          icon={GitPullRequest} 
          iconBg="#ecfdf5"
          color="#10b981" 
          sparklinePath="M0 28 Q 30 20, 70 24 T 130 10 T 170 16 L 200 6" 
        />

        <MetricCard 
          title="Files" 
          value={files.length} 
          badge="↑ 6%"
          subtitle="Indexed Code Modules" 
          icon={FileText} 
          iconBg="#fff7ed"
          color="#f59e0b" 
          sparklinePath="M0 22 Q 40 12, 80 26 T 140 14 T 180 8 L 200 4" 
        />
      </section>

      {/* Footer Copyright */}
      <footer className="overview-footer">
        © 2026 DevPulse. All rights reserved.
      </footer>
    </div>
  );
}
