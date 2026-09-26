// frontend/src/pages/users/UserList.jsx

import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import {
  fetchAllUsers,
  deleteUser,
  activateUser,
  deactivateUser,
  setSelectedUser,
  clearUserMessages,
} from '../../store/userSlice';

// ── Role badge colours ───────────────────────────────
const ROLE_COLOURS = {
  DOCTOR:           { bg: '#eff6ff', color: '#1d4ed8', border: '#bfdbfe' },
  BILLING_STAFF:    { bg: '#fefce8', color: '#a16207', border: '#fde68a' },
  LAB_STAFF:        { bg: '#f0fdf4', color: '#15803d', border: '#bbf7d0' },
  PHARMACY_STAFF:   { bg: '#fdf4ff', color: '#7e22ce', border: '#e9d5ff' },
  RECEPTIONIST:     { bg: '#fff7ed', color: '#c2410c', border: '#fed7aa' },
};

const ROLE_LABELS = {
  DOCTOR:           'Doctor',
  BILLING_STAFF:    'Billing Staff',
  LAB_STAFF:        'Lab Staff',
  PHARMACY_STAFF:   'Pharmacy Staff',
  RECEPTIONIST:     'Receptionist',
};

function RoleBadge({ role }) {
  const style = ROLE_COLOURS[role] || { bg: '#f1f5f9', color: '#475569', border: '#cbd5e1' };
  return (
    <span style={{
      padding: '2px 10px', borderRadius: 20, fontSize: 11, fontWeight: 700,
      background: style.bg, color: style.color, border: `1px solid ${style.border}`,
    }}>
      {ROLE_LABELS[role] || role}
    </span>
  );
}

function StatusBadge({ active }) {
  return active
    ? <span className="badge bg-success">Active</span>
    : <span className="badge bg-secondary">Inactive</span>;
}

export default function UserList() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { users, loading, deleteLoading, error, successMessage } = useSelector((s) => s.user);

  const [search, setSearch]       = useState('');
  const [roleFilter, setRoleFilter] = useState('');
  const [confirmId, setConfirmId] = useState(null);

  useEffect(() => {
    dispatch(fetchAllUsers());
  }, [dispatch]);

  useEffect(() => {
    return () => { dispatch(clearUserMessages()); };
  }, [dispatch]);

  const handleEdit = (user) => {
    dispatch(setSelectedUser(user));
    navigate(`/users/${user.userId}/edit`);
  };

  const handleDelete = () => {
    if (!confirmId) return;
    dispatch(deleteUser(confirmId)).then(() => setConfirmId(null));
  };

  const handleToggleActive = (user) => {
    if (user.active) {
      dispatch(deactivateUser(user.userId));
    } else {
      dispatch(activateUser(user.userId));
    }
  };

  // ── Filter logic ──────────────────────────────────
  const filtered = users.filter((u) => {
    const q = search.toLowerCase();
    const matchesSearch =
      u.username?.toLowerCase().includes(q) ||
      u.email?.toLowerCase().includes(q) ||
      ROLE_LABELS[u.role]?.toLowerCase().includes(q);
    const matchesRole = !roleFilter || u.role === roleFilter;
    return matchesSearch && matchesRole;
  });

  return (
    <div>
      {/* ── Header ── */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 20 }}>
        <div>
          <h5 style={{ margin: 0, fontWeight: 700, color: '#0f172a' }}>User Management</h5>
          <p style={{ margin: 0, fontSize: 13, color: '#64748b' }}>Manage hospital staff accounts and roles</p>
        </div>
        <button
          onClick={() => navigate('/users/new')}
          style={{
            padding: '8px 20px', borderRadius: 8, border: 'none',
            background: '#2563eb', color: '#fff', fontWeight: 600,
            fontSize: 13, cursor: 'pointer',
          }}
        >
          + Add User
        </button>
      </div>

      {/* ── Alerts ── */}
      {error && (
        <div className="alert alert-danger alert-dismissible" role="alert" style={{ marginBottom: 16 }}>
          {typeof error === 'string' ? error : 'An error occurred.'}
          <button type="button" className="btn-close" onClick={() => dispatch(clearUserMessages())} />
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success" role="alert" style={{ marginBottom: 16 }}>
          {successMessage}
        </div>
      )}

      {/* ── Filters ── */}
      <div style={{ display: 'flex', gap: 12, marginBottom: 16 }}>
        <input
          className="form-control form-control-sm"
          style={{ maxWidth: 280 }}
          placeholder="Search by name or email…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select
          className="form-select form-select-sm"
          style={{ maxWidth: 200 }}
          value={roleFilter}
          onChange={(e) => setRoleFilter(e.target.value)}
        >
          <option value="">All Roles</option>
          {Object.entries(ROLE_LABELS).map(([val, label]) => (
            <option key={val} value={val}>{label}</option>
          ))}
        </select>
      </div>

      {/* ── Table ── */}
      <div style={{
        background: '#fff', border: '1px solid #e2e8f0',
        borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)',
      }}>
        {loading ? (
          <div style={{ padding: 40, textAlign: 'center', color: '#64748b' }}>
            <span className="spinner-border spinner-border-sm me-2" />
            Loading users…
          </div>
        ) : filtered.length === 0 ? (
          <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8', fontSize: 14 }}>
            No users found.
          </div>
        ) : (
          <table className="table table-hover mb-0" style={{ fontSize: 13 }}>
            <thead style={{ background: '#f8fafc' }}>
              <tr>
                <th style={{ padding: '12px 16px', fontWeight: 600, color: '#475569', border: 'none' }}>#</th>
                <th style={{ padding: '12px 16px', fontWeight: 600, color: '#475569', border: 'none' }}>Username</th>
                <th style={{ padding: '12px 16px', fontWeight: 600, color: '#475569', border: 'none' }}>Email</th>
                <th style={{ padding: '12px 16px', fontWeight: 600, color: '#475569', border: 'none' }}>Role</th>
                <th style={{ padding: '12px 16px', fontWeight: 600, color: '#475569', border: 'none' }}>Status</th>
                <th style={{ padding: '12px 16px', fontWeight: 600, color: '#475569', border: 'none', textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((user, idx) => (
                <tr key={user.userId}>
                  <td style={{ padding: '12px 16px', color: '#94a3b8' }}>{idx + 1}</td>
                  <td style={{ padding: '12px 16px', fontWeight: 600, color: '#0f172a' }}>{user.username}</td>
                  <td style={{ padding: '12px 16px', color: '#475569' }}>{user.email || '—'}</td>
                  <td style={{ padding: '12px 16px' }}><RoleBadge role={user.role} /></td>
                  <td style={{ padding: '12px 16px' }}><StatusBadge active={user.active} /></td>
                  <td style={{ padding: '12px 16px', textAlign: 'right' }}>
                    <div style={{ display: 'flex', gap: 6, justifyContent: 'flex-end' }}>
                      {/* Toggle active/inactive */}
                      <button
                        onClick={() => handleToggleActive(user)}
                        title={user.active ? 'Deactivate' : 'Activate'}
                        style={{
                          padding: '4px 10px', fontSize: 11, borderRadius: 6, cursor: 'pointer',
                          border: `1px solid ${user.active ? '#fde68a' : '#bbf7d0'}`,
                          background: user.active ? '#fefce8' : '#f0fdf4',
                          color: user.active ? '#a16207' : '#15803d',
                          fontWeight: 600,
                        }}
                      >
                        {user.active ? 'Deactivate' : 'Activate'}
                      </button>

                      {/* Edit */}
                      <button
                        onClick={() => handleEdit(user)}
                        style={{
                          padding: '4px 10px', fontSize: 11, borderRadius: 6, cursor: 'pointer',
                          border: '1px solid #bfdbfe', background: '#eff6ff', color: '#1d4ed8', fontWeight: 600,
                        }}
                      >
                        Edit
                      </button>

                      {/* Delete */}
                      <button
                        onClick={() => setConfirmId(user.userId)}
                        disabled={deleteLoading}
                        style={{
                          padding: '4px 10px', fontSize: 11, borderRadius: 6, cursor: 'pointer',
                          border: '1px solid #fecaca', background: '#fef2f2', color: '#dc2626', fontWeight: 600,
                        }}
                      >
                        Delete
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* ── Delete Confirm Modal ── */}
      {confirmId && (
        <div
          style={{
            position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
            display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 999,
          }}
          onClick={() => setConfirmId(null)}
        >
          <div
            style={{
              background: '#fff', borderRadius: 12, padding: 28, maxWidth: 400, width: '90%',
              boxShadow: '0 20px 60px rgba(0,0,0,0.2)',
            }}
            onClick={(e) => e.stopPropagation()}
          >
            <h6 style={{ fontWeight: 700, color: '#0f172a', marginBottom: 8 }}>Delete User?</h6>
            <p style={{ fontSize: 13, color: '#64748b', marginBottom: 20 }}>
              This action cannot be undone. The user account will be permanently removed.
            </p>
            <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
              <button
                onClick={() => setConfirmId(null)}
                style={{
                  padding: '8px 18px', borderRadius: 8,
                  border: '1px solid #e2e8f0', background: '#fff',
                  cursor: 'pointer', fontSize: 13, color: '#475569',
                }}
              >
                Cancel
              </button>
              <button
                onClick={handleDelete}
                disabled={deleteLoading}
                style={{
                  padding: '8px 18px', borderRadius: 8, border: 'none',
                  background: '#dc2626', color: '#fff',
                  cursor: deleteLoading ? 'not-allowed' : 'pointer',
                  fontSize: 13, fontWeight: 600, opacity: deleteLoading ? 0.7 : 1,
                }}
              >
                {deleteLoading ? (
                  <><span className="spinner-border spinner-border-sm me-2" />Deleting…</>
                ) : 'Delete'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
