// frontend/src/store/index.js
// Add userReducer to existing store

import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import patientReducer from "./patientSlice";
import userReducer from "./userSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
    patient: patientReducer,
    user: userReducer,
  },
});

export default store;
