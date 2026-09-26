// frontend/src/store/userSlice.js

import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import userApi from '../api/userApi';

// ── Async Thunks ────────────────────────────────────────────────────────────

export const fetchAllUsers = createAsyncThunk(
  'user/fetchAll',
  async (_, { rejectWithValue }) => {
    try {
      const res = await userApi.getAllUsers();
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to fetch users');
    }
  }
);

export const fetchUserById = createAsyncThunk(
  'user/fetchById',
  async (userId, { rejectWithValue }) => {
    try {
      const res = await userApi.getUserById(userId);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to fetch user');
    }
  }
);

export const createUser = createAsyncThunk(
  'user/create',
  async (data, { rejectWithValue }) => {
    try {
      const res = await userApi.createUser(data);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to create user');
    }
  }
);

export const updateUser = createAsyncThunk(
  'user/update',
  async ({ userId, data }, { rejectWithValue }) => {
    try {
      const res = await userApi.updateUser(userId, data);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to update user');
    }
  }
);

export const activateUser = createAsyncThunk(
  'user/activate',
  async (userId, { rejectWithValue }) => {
    try {
      const res = await userApi.activateUser(userId);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to activate user');
    }
  }
);

export const deactivateUser = createAsyncThunk(
  'user/deactivate',
  async (userId, { rejectWithValue }) => {
    try {
      const res = await userApi.deactivateUser(userId);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to deactivate user');
    }
  }
);

export const deleteUser = createAsyncThunk(
  'user/delete',
  async (userId, { rejectWithValue }) => {
    try {
      await userApi.deleteUser(userId);
      return userId;
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || 'Failed to delete user');
    }
  }
);

// ── Slice ────────────────────────────────────────────────────────────────────

const userSlice = createSlice({
  name: 'user',
  initialState: {
    users: [],
    selectedUser: null,
    loading: false,
    formLoading: false,
    deleteLoading: false,
    error: null,
    successMessage: null,
  },
  reducers: {
    setSelectedUser(state, action) {
      state.selectedUser = action.payload;
    },
    clearSelectedUser(state) {
      state.selectedUser = null;
    },
    clearUserMessages(state) {
      state.error = null;
      state.successMessage = null;
    },
  },
  extraReducers: (builder) => {
    // fetchAll
    builder
      .addCase(fetchAllUsers.pending, (state) => { state.loading = true; state.error = null; })
      .addCase(fetchAllUsers.fulfilled, (state, action) => { state.loading = false; state.users = action.payload; })
      .addCase(fetchAllUsers.rejected, (state, action) => { state.loading = false; state.error = action.payload; });

    // fetchById
    builder
      .addCase(fetchUserById.pending, (state) => { state.loading = true; state.error = null; })
      .addCase(fetchUserById.fulfilled, (state, action) => { state.loading = false; state.selectedUser = action.payload; })
      .addCase(fetchUserById.rejected, (state, action) => { state.loading = false; state.error = action.payload; });

    // create
    builder
      .addCase(createUser.pending, (state) => { state.formLoading = true; state.error = null; state.successMessage = null; })
      .addCase(createUser.fulfilled, (state, action) => {
        state.formLoading = false;
        state.users.unshift(action.payload);
        state.successMessage = 'User created successfully';
      })
      .addCase(createUser.rejected, (state, action) => { state.formLoading = false; state.error = action.payload; });

    // update
    builder
      .addCase(updateUser.pending, (state) => { state.formLoading = true; state.error = null; state.successMessage = null; })
      .addCase(updateUser.fulfilled, (state, action) => {
        state.formLoading = false;
        const idx = state.users.findIndex((u) => u.userId === action.payload.userId);
        if (idx !== -1) state.users[idx] = action.payload;
        state.selectedUser = action.payload;
        state.successMessage = 'User updated successfully';
      })
      .addCase(updateUser.rejected, (state, action) => { state.formLoading = false; state.error = action.payload; });

    // activate / deactivate — update user in list
    const updateUserInList = (state, action) => {
      const idx = state.users.findIndex((u) => u.userId === action.payload.userId);
      if (idx !== -1) state.users[idx] = action.payload;
      if (state.selectedUser?.userId === action.payload.userId) state.selectedUser = action.payload;
    };
    builder
      .addCase(activateUser.fulfilled, updateUserInList)
      .addCase(deactivateUser.fulfilled, updateUserInList);

    // delete
    builder
      .addCase(deleteUser.pending, (state) => { state.deleteLoading = true; state.error = null; })
      .addCase(deleteUser.fulfilled, (state, action) => {
        state.deleteLoading = false;
        state.users = state.users.filter((u) => u.userId !== action.payload);
        if (state.selectedUser?.userId === action.payload) state.selectedUser = null;
        state.successMessage = 'User deleted successfully';
      })
      .addCase(deleteUser.rejected, (state, action) => { state.deleteLoading = false; state.error = action.payload; });
  },
});

export const { setSelectedUser, clearSelectedUser, clearUserMessages } = userSlice.actions;
export default userSlice.reducer;
