import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Overview from './pages/Overview';
import ExpertFinder from './pages/ExpertFinder';
import ReviewerRouter from './pages/ReviewerRouter';
import HealthRadar from './pages/HealthRadar';
import GraphExplorer from './pages/GraphExplorer';

export default function App() {
  const [activeTab, setActiveTab] = useState('Overview');

  // Real Backend API States
  const [health, setHealth] = useState({ connected: false, nodeCount: 0, relationshipCount: 0 });
  const [developers, setDevelopers] = useState([]);
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(false);

  // Feature Query States
  const [searchQuery, setSearchQuery] = useState('OrderService.java');
  const [expertQuery, setExpertQuery] = useState('OrderService.java');
  const [expertResult, setExpertResult] = useState(null);

  const [selectedFile, setSelectedFile] = useState('OrderService.java');
  const [reviewerResult, setReviewerResult] = useState(null);
  const [radarResult, setRadarResult] = useState(null);

  const getApiUrl = (endpoint) => `/api${endpoint}`;

  // Initial Fetch on Load
  useEffect(() => {
    loadAllData();
  }, []);

  const loadAllData = async () => {
    setLoading(true);
    await Promise.all([
      fetchHealth(),
      fetchDevelopers(),
      fetchFiles(),
      fetchExpertData(expertQuery),
      fetchReviewerData(selectedFile),
      fetchRadarData()
    ]);
    setLoading(false);
  };

  const fetchHealth = async () => {
    try {
      let res = await fetch(getApiUrl('/health'));
      if (!res.ok) res = await fetch('http://localhost:8080/api/health');
      if (res.ok) {
        const data = await res.json();
        setHealth(data);
      }
    } catch (err) {
      console.warn('Health check retry via direct backend URL');
    }
  };

  const fetchDevelopers = async () => {
    try {
      let res = await fetch(getApiUrl('/experts/developers'));
      if (!res.ok) res = await fetch('http://localhost:8080/api/experts/developers');
      if (res.ok) {
        const data = await res.json();
        setDevelopers(data);
      }
    } catch (err) {}
  };

  const fetchFiles = async () => {
    try {
      let res = await fetch(getApiUrl('/radar/files'));
      if (!res.ok) res = await fetch('http://localhost:8080/api/radar/files');
      if (res.ok) {
        const data = await res.json();
        setFiles(data);
      }
    } catch (err) {}
  };

  const fetchExpertData = async (query) => {
    try {
      let res = await fetch(getApiUrl(`/experts?query=${encodeURIComponent(query)}`));
      if (!res.ok) res = await fetch(`http://localhost:8080/api/experts?query=${encodeURIComponent(query)}`);
      if (res.ok) {
        const data = await res.json();
        setExpertResult(data);
      }
    } catch (err) {}
  };

  const fetchReviewerData = async (fileName) => {
    try {
      let res = await fetch(getApiUrl(`/reviewers/recommend?file=${encodeURIComponent(fileName)}`));
      if (!res.ok) res = await fetch(`http://localhost:8080/api/reviewers/recommend?file=${encodeURIComponent(fileName)}`);
      if (res.ok) {
        const data = await res.json();
        setReviewerResult(data);
      }
    } catch (err) {}
  };

  const fetchRadarData = async () => {
    try {
      let res = await fetch(getApiUrl('/radar/bus-factor'));
      if (!res.ok) res = await fetch('http://localhost:8080/api/radar/bus-factor');
      if (res.ok) {
        const data = await res.json();
        setRadarResult(data);
      }
    } catch (err) {}
  };

  const renderActivePage = () => {
    switch (activeTab) {
      case 'Expert Finder':
        return (
          <ExpertFinder 
            query={expertQuery} 
            setQuery={setExpertQuery} 
            onSearch={fetchExpertData} 
            result={expertResult} 
          />
        );

      case 'Reviewer Router':
        return (
          <ReviewerRouter 
            selectedFile={selectedFile} 
            setSelectedFile={setSelectedFile} 
            onSelect={fetchReviewerData} 
            result={reviewerResult} 
          />
        );

      case 'Health Radar':
        return <HealthRadar result={radarResult} />;

      case 'Graph Explorer':
        return <GraphExplorer />;

      case 'Overview':
      default:
        return (
          <Overview 
            developers={developers} 
            files={files} 
            expertResult={expertResult} 
            expertQuery={expertQuery} 
            setExpertQuery={setExpertQuery} 
            onExpertSearch={fetchExpertData} 
            selectedFile={selectedFile} 
            setSelectedFile={setSelectedFile} 
            reviewerResult={reviewerResult} 
            onReviewerSelect={fetchReviewerData} 
            radarResult={radarResult} 
            setActiveTab={setActiveTab} 
          />
        );
    }
  };

  return (
    <div className="app-layout">
      {/* Sidebar Navigation */}
      <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />

      {/* Main Workspace */}
      <main className="main-content">
        <Header 
          searchQuery={searchQuery} 
          setSearchQuery={setSearchQuery} 
          onSearchSubmit={(q) => {
            setExpertQuery(q);
            fetchExpertData(q);
            setActiveTab('Expert Finder');
          }} 
          health={health} 
          loading={loading} 
          onRefresh={loadAllData} 
        />

        <section className="welcome-section">
          <h1 className="welcome-title">Welcome back, John! 👋</h1>
          <p className="welcome-subtitle">Here's what's happening in your engineering graph.</p>
        </section>

        {renderActivePage()}
      </main>
    </div>
  );
}
