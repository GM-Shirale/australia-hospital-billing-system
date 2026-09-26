// frontend/src/router.jsx
// Central routing configuration — no BrowserRouter here (it lives in main.jsx).

import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

import AppLayout from "./components/layout/AppLayout";

// ── Pages ─────────────────────────────────────────────
import Login      from "./pages/auth/Login";

import PatientList   from "./pages/patients/PatientList";
import PatientForm   from "./pages/patients/PatientForm";
import PatientDetail from "./pages/patients/PatientDetail";

import UserList from "./pages/users/UserList";
import UserForm from "./pages/users/UserForm";

// ── Auth guard ────────────────────────────────────────
// Reads from localStorage so it works even before Redux hydrates.
const RequireAuth = ({ children }) => {
  const stored = localStorage.getItem("currentUser");
  if (!stored) return <Navigate to="/login" replace />;
  return <AppLayout>{children}</AppLayout>;
};

// ─────────────────────────────────────────────────────
export default function AppRouter() {
  return (
    <Routes>
      {/* ── Public ── */}
      <Route path="/login" element={<Login />} />

      {/* ── Patients ── */}
      <Route path="/patients"               element={<RequireAuth><PatientList /></RequireAuth>} />
      <Route path="/patients/new"           element={<RequireAuth><PatientForm /></RequireAuth>} />
      <Route path="/patients/:patientId"    element={<RequireAuth><PatientDetail /></RequireAuth>} />
      <Route path="/patients/:patientId/edit" element={<RequireAuth><PatientForm /></RequireAuth>} />

      {/* ── Users ── */}
      <Route path="/users"              element={<RequireAuth><UserList /></RequireAuth>} />
      <Route path="/users/new"          element={<RequireAuth><UserForm /></RequireAuth>} />
      <Route path="/users/:userId/edit" element={<RequireAuth><UserForm /></RequireAuth>} />

      {/* ── Default ── */}
      <Route path="/"  element={<Navigate to="/patients" replace />} />
      <Route path="*"  element={<Navigate to="/patients" replace />} />
    </Routes>
  );
}
