import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8082/api",
  headers: {
    "Content-Type": "application/json",
  },
});

axiosClient.interceptors.request.use(
  (config) => {
    // Token is stored inside the currentUser object by authSlice
    try {
      const raw = localStorage.getItem('currentUser');
      if (raw) {
        const { token } = JSON.parse(raw);
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
    } catch {
      // ignore parse errors
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// Auto-redirect to login on 401
axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('currentUser');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  },
);

export default axiosClient;
