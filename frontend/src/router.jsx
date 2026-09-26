// frontend/src/router.jsx
// Central route definitions — imported once by main.jsx

import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

// ── Pages ──────────────────────────────────────────────
import Login        from "./pages/Login";
import PatientList  from "./pages/Patients/PatientList";
import PatientForm  from "./pages/Patients/PatientForm";
import PatientDetail from "./pages/Patients/PatientDetail";
import UserList     from "./pages/Users/UserList";
import UserForm     from "./pages/Users/UserForm";

// ── Layout shell (sidebar + header) ───────────────────
import AppLayout   from "./components/AppLayout";

// ── Auth guard ─────────────────────────────────────────
const RequireAuth = ({ children }) => {
  // During Phase 1 (security disabled) we check localStorage directly
  const stored = localStorage.getItem("currentUser");
  if (!stored) return <Navigate to="/login" replace />;
  return <AppLayout>{children}</AppLayout>;
};

// ── Router ─────────────────────────────────────────────
export default function AppRouter() {
  return (
    <Routes>
      {/* ── Public ──────────────────────────────────── */}
      <Route path="/login" element={<Login />} />

      {/* ── Patients ────────────────────────────────── */}
      <Route
        path="/patients"
        element={<RequireAuth><PatientList /></RequireAuth>}
      />
      <Route
        path="/patients/new"
        element={<RequireAuth><PatientForm /></RequireAuth>}
      />
      <Route
        path="/patients/:patientId"
        element={<RequireAuth><PatientDetail /></RequireAuth>}
      />
      <Route
        path="/patients/:patientId/edit"
        element={<RequireAuth><PatientForm /></RequireAuth>}
      />

      {/* ── Users (staff registration) ──────────────── */}
      <Route
        path="/users"
        element={<RequireAuth><UserList /></RequireAuth>}
      />
      <Route
        path="/users/new"
        element={<RequireAuth><UserForm /></RequireAuth>}
      />
      <Route
        path="/users/:userId/edit"
        element={<RequireAuth><UserForm /></RequireAuth>}
      />

      {/* ── Defaults ────────────────────────────────── */}
      <Route path="/"   element={<Navigate to="/patients" replace />} />
      <Route path="*"   element={<Navigate to="/patients" replace />} />
    </Routes>
  );
}
