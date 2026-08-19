import React, { useState, useEffect } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Overview from './pages/Overview';
import ExpertFinder from './pages/ExpertFinder';
import ReviewerRouter from './pages/ReviewerRouter';
import HealthRadar from './pages/HealthRadar';
import Repositories from './pages/Repositories';
import Developers from './pages/Developers';

export default function App() {
  const navigate = useNavigate();

  // Dark Mode State
  const [darkMode, setDarkMode] = useState(false);

  useEffect(() => {
    if (darkMode) {
      document.body.classList.add('dark-mode');
    } else {
      document.body.classList.remove('dark-mode');
    }
  }, [darkMode]);

  // Real Backend API States
  const [health, setHealth] = useState({ connected: false, nodeCount: 0, relationshipCount: 0 });
  const [developers, setDevelopers] = useState([]);
  const [files, setFiles] = useState([]);
  const [repositories, setRepositories] = useState([]);
  const [pullRequests, setPullRequests] = useState([]);
  const [loading, setLoading] = useState(false);

  // Feature Query States
  const [searchQuery, setSearchQuery] = useState('');
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
      fetchRepositories(),
      fetchPullRequests(),
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
    } catch (err) {}
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

  const fetchRepositories = async () => {
    try {
      let res = await fetch(getApiUrl('/repositories'));
      if (!res.ok) res = await fetch('http://localhost:8080/api/repositories');
      if (res.ok) {
        const data = await res.json();
        setRepositories(data);
      }
    } catch (err) {}
  };

  const fetchPullRequests = async () => {
    try {
      let res = await fetch(getApiUrl('/pull-requests'));
      if (!res.ok) res = await fetch('http://localhost:8080/api/pull-requests');
      if (res.ok) {
        const data = await res.json();
        setPullRequests(data);
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

  return (
    <div className="app-layout">
      {/* Sidebar Navigation */}
      <Sidebar darkMode={darkMode} setDarkMode={setDarkMode} />

      {/* Main Workspace */}
      <main className="main-content">
        <Header 
          searchQuery={searchQuery} 
          setSearchQuery={setSearchQuery} 
          onSearchSubmit={(q) => {
            setExpertQuery(q);
            fetchExpertData(q);
            navigate('/experts');
          }} 
          health={health} 
          loading={loading} 
          onRefresh={loadAllData} 
        />

        <Routes>
          <Route 
            path="/" 
            element={
              <Overview 
                developers={developers} 
                files={files} 
                repositories={repositories}
                pullRequests={pullRequests}
              />
            } 
          />
          <Route 
            path="/experts" 
            element={
              <ExpertFinder 
                query={expertQuery} 
                setQuery={setExpertQuery} 
                onSearch={fetchExpertData} 
                result={expertResult} 
              />
            } 
          />
          <Route 
            path="/reviewers" 
            element={
              <ReviewerRouter 
                selectedFile={selectedFile} 
                setSelectedFile={setSelectedFile} 
                onSelect={fetchReviewerData} 
                result={reviewerResult} 
                files={files}
              />
            } 
          />
          <Route path="/radar" element={<HealthRadar result={radarResult} />} />
          <Route path="/repositories" element={<Repositories repositories={repositories} files={files} />} />
          <Route path="/developers" element={<Developers developers={developers} />} />
        </Routes>
      </main>
    </div>
  );
}
