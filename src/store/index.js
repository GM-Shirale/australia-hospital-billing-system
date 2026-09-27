// frontend/src/store/index.js

import { configureStore } from '@reduxjs/toolkit';
import authReducer    from './authSlice';
import patientReducer from './patientSlice';
import userReducer    from './userSlice';

const store = configureStore({
  reducer: {
    auth:    authReducer,
    patient: patientReducer,
    user:    userReducer,
  },
});

export default store;
