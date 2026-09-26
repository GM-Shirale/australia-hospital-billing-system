// frontend/src/components/AppLayout.jsx
// Wrapper that renders the sidebar + header shell around every protected page.
// Reads the existing Layout component and wires up sidebar navigation via
// React Router so clicking a nav item navigates to the correct route.

import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import HospitalLogo from "./HospitalLogo";
import {
  LayoutDashboard,
  Building2,
  Stethoscope,
  Bed,
  Receipt,
  LogOut,
  Users,
  FileText,
  FlaskConical,
  ShieldCheck,
  Pill,
  RefreshCw,
  History,
  UserCog,
} from "lucide-react";

// ── Sidebar nav items ──────────────────────────────────
const NAV_ITEMS = [
  { key: "dashboard",      label: "Overview Dashboard",     icon: LayoutDashboard, path: "/dashboard",   roles: [] },
  { key: "users",          label: "User Management",        icon: UserCog,          path: "/users",       roles: ["ADMIN"] },
  { key: "doctors",        label: "Doctors & Providers",    icon: Stethoscope,      path: "/doctors",     roles: ["ADMIN", "DOCTOR"] },
  { key: "departments",    label: "Departments",            icon: Building2,        path: "/departments", roles: ["ADMIN"] },
  { key: "patients",       label: "Patients & Admission",   icon: Users,            path: "/patients",    roles: [] },
  { key: "medical-history",label: "Patient Medical History",icon: History,          path: "/medical-history", roles: [] },
  { key: "rooms",          label: "Rooms & Beds",           icon: Bed,              path: "/rooms",       roles: ["ADMIN", "BILLING_STAFF"] },
  { key: "pharmacy",       label: "Pharmacy & Stock",       icon: Pill,             path: "/pharmacy",    roles: [] },
  { key: "laboratory",     label: "Laboratory & Pathology", icon: FlaskConical,     path: "/laboratory",  roles: [] },
  { key: "billing",        label: "Central Billing Engine", icon: Receipt,          path: "/billing",     roles: ["ADMIN", "BILLING_STAFF"] },
  { key: "insurance",      label: "Insurance & Claims",     icon: ShieldCheck,      path: "/insurance",   roles: [] },
  { key: "invoices",       label: "Invoices & Discharge",   icon: FileText,         path: "/invoices",    roles: ["ADMIN", "BILLING_STAFF"] },
];

export default function AppLayout({ children }) {
  const navigate  = useNavigate();
  const location  = useLocation();

  const user = JSON.parse(localStorage.getItem("currentUser")) || { role: "ADMIN", username: "admin" };
  const role = user.role || "ADMIN";

  const handleLogout = () => {
    localStorage.removeItem("currentUser");
    navigate("/login");
  };

  // Derive active key from current path
  const activeKey = NAV_ITEMS.find((n) =>
    location.pathname.startsWith(n.path)
  )?.key || "dashboard";

  // Filter nav items by role (empty roles array = visible to all)
  const visibleNav = NAV_ITEMS.filter(
    (n) => n.roles.length === 0 || n.roles.includes(role)
  );

  return (
    <div style={{ display: "flex", flexDirection: "column", height: "100vh", overflow: "hidden", backgroundColor: "#f8fafc" }}>

      {/* ── Header ── */}
      <header style={{
        height: 64,
        backgroundColor: "#fff",
        borderBottom: "1px solid #e2e8f0",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "0 24px",
        flexShrink: 0,
        zIndex: 30,
        boxShadow: "0 1px 3px rgba(0,0,0,0.06)",
      }}>
        <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
          <HospitalLogo />
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
          <div style={{
            display: "flex", alignItems: "center", gap: 8,
            background: "#f8fafc", border: "1px solid #e2e8f0",
            padding: "4px 14px", borderRadius: 999,
            fontSize: 12, fontWeight: 600, color: "#475569",
          }}>
            <span style={{ width: 8, height: 8, borderRadius: "50%", background: "#22c55e" }} />
            <span style={{ color: "#94a3b8", fontWeight: 400 }}>Facility:</span> Sydney Central Hospital
          </div>

          <button
            onClick={() => window.location.reload()}
            style={{ padding: 8, background: "#f8fafc", border: "1px solid #e2e8f0", borderRadius: 10, cursor: "pointer", color: "#64748b" }}
            title="Refresh"
          >
            <RefreshCw size={14} />
          </button>

          <div style={{ textAlign: "right", paddingLeft: 8, borderLeft: "1px solid #e2e8f0" }}>
            <p style={{ margin: 0, fontSize: 12, fontWeight: 700, color: "#0f172a" }}>{user.username || user.email}</p>
            <span style={{
              fontSize: 10, fontWeight: 700, padding: "1px 8px",
              borderRadius: 4, background: "#eff6ff", color: "#2563eb",
              border: "1px solid #bfdbfe",
            }}>
              {role}
            </span>
          </div>

          <button
            onClick={handleLogout}
            style={{
              display: "flex", alignItems: "center", gap: 6,
              fontSize: 12, color: "#e11d48", fontWeight: 700,
              background: "none", border: "1px solid #fecdd3",
              padding: "6px 12px", borderRadius: 10, cursor: "pointer",
            }}
          >
            <LogOut size={13} /> Logout
          </button>
        </div>
      </header>

      {/* ── Body: sidebar + main ── */}
      <div style={{ display: "flex", flex: 1, overflow: "hidden" }}>

        {/* Sidebar */}
        <aside style={{
          width: 240,
          backgroundColor: "#fff",
          borderRight: "1px solid #e2e8f0",
          padding: "12px 8px",
          overflowY: "auto",
          flexShrink: 0,
        }}>
          {visibleNav.map((item) => {
            const Icon = item.icon;
            const active = activeKey === item.key;
            return (
              <button
                key={item.key}
                onClick={() => navigate(item.path)}
                style={{
                  width: "100%", display: "flex", alignItems: "center",
                  gap: 10, padding: "10px 14px", borderRadius: 10,
                  marginBottom: 4, border: "none", cursor: "pointer",
                  fontSize: 13, fontWeight: 600, textAlign: "left",
                  background: active ? "#2563eb" : "none",
                  color: active ? "#fff" : "#475569",
                  transition: "background 0.15s",
                }}
                onMouseEnter={(e) => { if (!active) e.currentTarget.style.background = "#f1f5f9"; }}
                onMouseLeave={(e) => { if (!active) e.currentTarget.style.background = "none"; }}
              >
                <Icon size={15} />
                <span>{item.label}</span>
              </button>
            );
          })}
        </aside>

        {/* Main content — scrollable */}
        <main style={{ flex: 1, overflowY: "auto", padding: "28px 32px" }}>
          {children}
        </main>
      </div>
    </div>
  );
}
