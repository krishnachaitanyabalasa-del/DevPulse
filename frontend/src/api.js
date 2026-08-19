import axios from 'axios';

const API_BASE_URL = '/api';
const BACKEND_URL = 'http://localhost:8080/api';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const fetchApi = async (endpoint) => {
  try {
    const response = await apiClient.get(endpoint);
    return response.data;
  } catch (error) {
    const fallbackResponse = await axios.get(`${BACKEND_URL}${endpoint}`);
    return fallbackResponse.data;
  }
};

export const postApi = async (endpoint, payload) => {
  try {
    const response = await apiClient.post(endpoint, payload);
    return response.data;
  } catch (error) {
    const fallbackResponse = await axios.post(`${BACKEND_URL}${endpoint}`, payload);
    return fallbackResponse.data;
  }
};
