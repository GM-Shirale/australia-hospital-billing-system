import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import authApi from '../api/authApi';

// ── Async thunks ──────────────────────────────────────────────────────────────

export const adminLogin = createAsyncThunk(
  'auth/adminLogin',
  async (credentials, { rejectWithValue }) => {
    try {
      const res = await authApi.adminLogin(credentials);
      return res.data; // { token, username, role }
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || err.response?.data || 'Login failed'
      );
    }
  }
);

export const userLogin = createAsyncThunk(
  'auth/userLogin',
  async (credentials, { rejectWithValue }) => {
    try {
      const res = await authApi.userLogin(credentials);
      return res.data; // { token, username, role }
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || err.response?.data || 'Login failed'
      );
    }
  }
);

export const logoutUser = createAsyncThunk(
  'auth/logout',
  async (_, { rejectWithValue }) => {
    try {
      await authApi.logout();
    } catch {
      // Even if backend call fails, clear local session
    }
  }
);

// ── Helpers ───────────────────────────────────────────────────────────────────

const loadFromStorage = () => {
  try {
    const raw = localStorage.getItem('currentUser');
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
};

// ── Slice ─────────────────────────────────────────────────────────────────────

const stored = loadFromStorage();

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    token: stored?.token || null,
    username: stored?.username || null,
    role: stored?.role || null,
    isAuthenticated: !!stored?.token,
    loading: false,
    error: null,
  },
  reducers: {
    clearAuthError(state) {
      state.error = null;
    },
    // For manual logout without calling the API
    clearAuth(state) {
      state.token = null;
      state.username = null;
      state.role = null;
      state.isAuthenticated = false;
      localStorage.removeItem('currentUser');
    },
  },
  extraReducers: (builder) => {
    // ── Helper to handle both login thunks the same way ──
    const handlePending = (state) => {
      state.loading = true;
      state.error = null;
    };
    const handleFulfilled = (state, action) => {
      const { token, username, role } = action.payload;
      state.token = token;
      state.username = username;
      state.role = role;
      state.isAuthenticated = true;
      state.loading = false;
      state.error = null;
      // Persist to localStorage so RequireAuth + axiosClient pick it up
      localStorage.setItem('currentUser', JSON.stringify({ token, username, role }));
    };
    const handleRejected = (state, action) => {
      state.loading = false;
      state.error =
        typeof action.payload === 'string'
          ? action.payload
          : 'Invalid username or password';
    };

    builder
      .addCase(adminLogin.pending, handlePending)
      .addCase(adminLogin.fulfilled, handleFulfilled)
      .addCase(adminLogin.rejected, handleRejected)

      .addCase(userLogin.pending, handlePending)
      .addCase(userLogin.fulfilled, handleFulfilled)
      .addCase(userLogin.rejected, handleRejected)

      .addCase(logoutUser.fulfilled, (state) => {
        state.token = null;
        state.username = null;
        state.role = null;
        state.isAuthenticated = false;
        localStorage.removeItem('currentUser');
      });
  },
});

export const { clearAuthError, clearAuth } = authSlice.actions;
export default authSlice.reducer;
