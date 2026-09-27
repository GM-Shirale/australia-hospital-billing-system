import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import patientApi from '../api/patientApi';

// ─── Async Thunks ────────────────────────────────────────────────────────────

export const fetchAllPatients = createAsyncThunk(
  'patient/fetchAll',
  async (_, { rejectWithValue }) => {
    try {
      const res = await patientApi.getAll();
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || 'Failed to fetch patients'
      );
    }
  }
);

export const fetchPatientById = createAsyncThunk(
  'patient/fetchById',
  async (patientId, { rejectWithValue }) => {
    try {
      const res = await patientApi.getById(patientId);
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || 'Failed to fetch patient'
      );
    }
  }
);

export const createPatient = createAsyncThunk(
  'patient/create',
  async (data, { rejectWithValue }) => {
    try {
      const res = await patientApi.create(data);
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || 'Failed to create patient'
      );
    }
  }
);

export const updatePatient = createAsyncThunk(
  'patient/update',
  async ({ patientId, data }, { rejectWithValue }) => {
    try {
      const res = await patientApi.update(patientId, data);
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || 'Failed to update patient'
      );
    }
  }
);

export const deletePatient = createAsyncThunk(
  'patient/delete',
  async (patientId, { rejectWithValue }) => {
    try {
      await patientApi.remove(patientId);
      return patientId;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || 'Failed to delete patient'
      );
    }
  }
);

// ─── Slice ───────────────────────────────────────────────────────────────────

const patientSlice = createSlice({
  name: 'patient',
  initialState: {
    // list of all patients
    patients: [],
    // currently selected / viewed patient (global — used by other modules)
    selectedPatient: null,
    // selectedPatientId kept separately for easy cross-module access
    selectedPatientId: null,
    // loading flags
    loading: false,
    formLoading: false,
    deleteLoading: false,
    // error message
    error: null,
    // success message
    successMessage: null,
  },
  reducers: {
    // Set selected patient globally (used by other modules to pick up patientId)
    setSelectedPatient(state, action) {
      state.selectedPatient = action.payload;
      state.selectedPatientId = action.payload?.patientId ?? null;
    },
    clearSelectedPatient(state) {
      state.selectedPatient = null;
      state.selectedPatientId = null;
    },
    clearMessages(state) {
      state.error = null;
      state.successMessage = null;
    },
  },
  extraReducers: (builder) => {
    // ── fetchAll ──
    builder
      .addCase(fetchAllPatients.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAllPatients.fulfilled, (state, action) => {
        state.loading = false;
        state.patients = action.payload;
      })
      .addCase(fetchAllPatients.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // ── fetchById ──
    builder
      .addCase(fetchPatientById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPatientById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedPatient = action.payload;
        state.selectedPatientId = action.payload.patientId;
      })
      .addCase(fetchPatientById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // ── create ──
    builder
      .addCase(createPatient.pending, (state) => {
        state.formLoading = true;
        state.error = null;
        state.successMessage = null;
      })
      .addCase(createPatient.fulfilled, (state, action) => {
        state.formLoading = false;
        state.patients.unshift(action.payload);
        state.selectedPatient = action.payload;
        state.selectedPatientId = action.payload.patientId;
        state.successMessage = 'Patient registered successfully';
      })
      .addCase(createPatient.rejected, (state, action) => {
        state.formLoading = false;
        state.error = action.payload;
      });

    // ── update ──
    builder
      .addCase(updatePatient.pending, (state) => {
        state.formLoading = true;
        state.error = null;
        state.successMessage = null;
      })
      .addCase(updatePatient.fulfilled, (state, action) => {
        state.formLoading = false;
        const idx = state.patients.findIndex(
          (p) => p.patientId === action.payload.patientId
        );
        if (idx !== -1) state.patients[idx] = action.payload;
        state.selectedPatient = action.payload;
        state.selectedPatientId = action.payload.patientId;
        state.successMessage = 'Patient updated successfully';
      })
      .addCase(updatePatient.rejected, (state, action) => {
        state.formLoading = false;
        state.error = action.payload;
      });

    // ── delete ──
    builder
      .addCase(deletePatient.pending, (state) => {
        state.deleteLoading = true;
        state.error = null;
      })
      .addCase(deletePatient.fulfilled, (state, action) => {
        state.deleteLoading = false;
        state.patients = state.patients.filter(
          (p) => p.patientId !== action.payload
        );
        if (state.selectedPatientId === action.payload) {
          state.selectedPatient = null;
          state.selectedPatientId = null;
        }
        state.successMessage = 'Patient deleted successfully';
      })
      .addCase(deletePatient.rejected, (state, action) => {
        state.deleteLoading = false;
        state.error = action.payload;
      });
  },
});

export const { setSelectedPatient, clearSelectedPatient, clearMessages } =
  patientSlice.actions;

export default patientSlice.reducer;
