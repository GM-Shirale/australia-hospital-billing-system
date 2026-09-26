import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  fetchAllUsers,
  deleteUser,
  activateUser,
  deactivateUser,
  clearUserError,
} from "../../store/userSlice";

// Role badge colours
const roleBadge = {
  RECEPTIONIST: "bg-info text-dark",
  DOCTOR: "bg-primary",
  BILLING_STAFF: "bg-warning text-dark",
  LAB_STAFF: "bg-secondary",
  PHARMACY_STAFF: "bg-success",
};

const roleLabel = {
  RECEPTIONIST: "Receptionist",
  DOCTOR: "Doctor",
  BILLING_STAFF: "Billing Staff",
  LAB_STAFF: "Lab Staff",
  PHARMACY_STAFF: "Pharmacy Staff",
};

const UserList = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { users, loading, deleteLoading, error } = useSelector(
    (s) => s.user
  );

  const [search, setSearch] = useState("");
  const [filterRole, setFilterRole] = useState("");
  const [confirmDelete, setConfirmDelete] = useState(null); // userId to delete

  useEffect(() => {
    dispatch(fetchAllUsers());
  }, [dispatch]);

  // ---- filter ----
  const filtered = users.filter((u) => {
    const q = search.toLowerCase();
    const matchSearch =
      u.username?.toLowerCase().includes(q) ||
      u.email?.toLowerCase().includes(q);
    const matchRole = filterRole ? u.role === filterRole : true;
    return matchSearch && matchRole;
  });

  // ---- handlers ----
  const handleDelete = (userId) => {
    dispatch(deleteUser(userId));
    setConfirmDelete(null);
  };

  const handleToggleStatus = (user) => {
    if (user.active) {
      dispatch(deactivateUser(user.userId));
    } else {
      dispatch(activateUser(user.userId));
    }
  };

  return (
    <div className="container-fluid py-4">
      {/* Header */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 className="mb-0 fw-bold">User Management</h4>
          <small className="text-muted">
            Manage staff accounts and role assignments
          </small>
        </div>
        <button
          className="btn btn-primary"
          onClick={() => navigate("/users/new")}
        >
          + Register New User
        </button>
      </div>

      {/* Error alert */}
      {error && (
        <div
          className="alert alert-danger alert-dismissible"
          role="alert"
        >
          {error}
          <button
            type="button"
            className="btn-close"
            onClick={() => dispatch(clearUserError())}
          />
        </div>
      )}

      {/* Filters */}
      <div className="card mb-4 border-0 shadow-sm">
        <div className="card-body py-3">
          <div className="row g-3">
            <div className="col-md-6">
              <input
                type="text"
                className="form-control"
                placeholder="Search by username or email..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
            <div className="col-md-4">
              <select
                className="form-select"
                value={filterRole}
                onChange={(e) => setFilterRole(e.target.value)}
              >
                <option value="">All Roles</option>
                <option value="RECEPTIONIST">Receptionist</option>
                <option value="DOCTOR">Doctor</option>
                <option value="BILLING_STAFF">Billing Staff</option>
                <option value="LAB_STAFF">Lab Staff</option>
                <option value="PHARMACY_STAFF">Pharmacy Staff</option>
              </select>
            </div>
            <div className="col-md-2 d-flex align-items-center">
              <small className="text-muted">
                {filtered.length} user{filtered.length !== 1 ? "s" : ""}
              </small>
            </div>
          </div>
        </div>
      </div>

      {/* Table */}
      <div className="card border-0 shadow-sm">
        <div className="card-body p-0">
          {loading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-primary" role="status" />
              <p className="mt-2 text-muted">Loading users...</p>
            </div>
          ) : filtered.length === 0 ? (
            <div className="text-center py-5 text-muted">
              <p className="mb-0">No users found.</p>
            </div>
          ) : (
            <div className="table-responsive">
              <table className="table table-hover mb-0 align-middle">
                <thead className="table-light">
                  <tr>
                    <th>#</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th className="text-end">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((user, idx) => (
                    <tr key={user.userId}>
                      <td className="text-muted">{idx + 1}</td>
                      <td className="fw-semibold">{user.username}</td>
                      <td>{user.email}</td>
                      <td>
                        <span
                          className={`badge rounded-pill ${
                            roleBadge[user.role] || "bg-secondary"
                          }`}
                        >
                          {roleLabel[user.role] || user.role}
                        </span>
                      </td>
                      <td>
                        <span
                          className={`badge ${
                            user.active ? "bg-success" : "bg-danger"
                          }`}
                        >
                          {user.active ? "Active" : "Inactive"}
                        </span>
                      </td>
                      <td className="text-end">
                        {/* Toggle Active/Inactive */}
                        <button
                          className={`btn btn-sm me-1 ${
                            user.active
                              ? "btn-outline-warning"
                              : "btn-outline-success"
                          }`}
                          title={user.active ? "Deactivate" : "Activate"}
                          onClick={() => handleToggleStatus(user)}
                        >
                          {user.active ? "Deactivate" : "Activate"}
                        </button>

                        {/* Edit */}
                        <button
                          className="btn btn-sm btn-outline-primary me-1"
                          onClick={() =>
                            navigate(`/users/${user.userId}/edit`)
                          }
                        >
                          Edit
                        </button>

                        {/* Delete */}
                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => setConfirmDelete(user.userId)}
                          disabled={!!deleteLoading[user.userId]}
                        >
                          {deleteLoading[user.userId] ? (
                            <span
                              className="spinner-border spinner-border-sm"
                              role="status"
                            />
                          ) : (
                            "Delete"
                          )}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Delete Confirm Modal */}
      {confirmDelete && (
        <div
          className="modal show d-block"
          style={{ backgroundColor: "rgba(0,0,0,0.5)" }}
        >
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Confirm Delete</h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setConfirmDelete(null)}
                />
              </div>
              <div className="modal-body">
                Are you sure you want to delete this user? This action
                cannot be undone.
              </div>
              <div className="modal-footer">
                <button
                  className="btn btn-secondary"
                  onClick={() => setConfirmDelete(null)}
                >
                  Cancel
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(confirmDelete)}
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserList;
