import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { format, parseISO, isValid } from 'date-fns';

import {
  fetchAllPatients,
  deletePatient,
  setSelectedPatient,
  clearMessages,
} from '../../store/patientSlice';

// ── Helpers ──────────────────────────────────────────────────────────────────

const formatDob = (dob) => {
  if (!dob) return '—';
  const d = parseISO(dob);
  return isValid(d) ? format(d, 'dd/MM/yyyy') : '—';
};

const statusBadge = (status) => {
  const map = {
    ACTIVE: 'success',
    INACTIVE: 'secondary',
    DECEASED: 'dark',
  };
  return (
    <span className={`badge bg-${map[status] || 'secondary'}`}>
      {status || '—'}
    </span>
  );
};

const genderLabel = {
  MALE: 'Male',
  FEMALE: 'Female',
  OTHER: 'Other',
  NOT_SPECIFIED: 'Not Specified',
};

// ─────────────────────────────────────────────────────────────────────────────

export default function PatientList() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { patients, loading, deleteLoading, error, successMessage } =
    useSelector((state) => state.patient);

  // patientId pending deletion (to show spinner on that row only)
  const [deletingId, setDeletingId] = useState(null);
  // patientId of the confirm-delete modal
  const [confirmId, setConfirmId] = useState(null);

  // Search / filter
  const [search, setSearch] = useState('');

  useEffect(() => {
    dispatch(fetchAllPatients());
  }, [dispatch]);

  // Clear success/error on unmount
  useEffect(() => {
    return () => {
      dispatch(clearMessages());
    };
  }, [dispatch]);

  const handleView = (patient) => {
    dispatch(setSelectedPatient(patient));
    navigate(`/patients/${patient.patientId}`);
  };

  const handleEdit = (patient) => {
    dispatch(setSelectedPatient(patient));
    navigate(`/patients/${patient.patientId}/edit`);
  };

  const handleDeleteConfirm = (patientId) => {
    setConfirmId(patientId);
  };

  const handleDeleteExecute = async () => {
    if (!confirmId) return;
    setDeletingId(confirmId);
    setConfirmId(null);
    await dispatch(deletePatient(confirmId));
    setDeletingId(null);
  };

  // Client-side search across name, patient number, email
  const filtered = patients.filter((p) => {
    const q = search.toLowerCase();
    return (
      !q ||
      `${p.firstName} ${p.middleName || ''} ${p.lastName}`.toLowerCase().includes(q) ||
      (p.patientNumber || '').toLowerCase().includes(q) ||
      (p.email || '').toLowerCase().includes(q) ||
      (p.medicareNumber || '').toLowerCase().includes(q)
    );
  });

  return (
    <div className="container-fluid py-4">
      {/* ── Header ── */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 className="mb-0 fw-bold">Patients</h4>
          <small className="text-muted">
            {patients.length} patient{patients.length !== 1 ? 's' : ''} registered
          </small>
        </div>
        <button
          className="btn btn-primary"
          onClick={() => navigate('/patients/new')}
        >
          + Register New Patient
        </button>
      </div>

      {/* ── Alert messages ── */}
      {error && (
        <div className="alert alert-danger alert-dismissible" role="alert">
          {error}
          <button
            type="button"
            className="btn-close"
            onClick={() => dispatch(clearMessages())}
          />
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success alert-dismissible" role="alert">
          {successMessage}
          <button
            type="button"
            className="btn-close"
            onClick={() => dispatch(clearMessages())}
          />
        </div>
      )}

      {/* ── Search bar ── */}
      <div className="row mb-3">
        <div className="col-md-4">
          <input
            type="text"
            className="form-control"
            placeholder="Search by name, patient no., email, Medicare..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      {/* ── Loading state ── */}
      {loading && (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status" />
          <p className="text-muted mt-2">Loading patients...</p>
        </div>
      )}

      {/* ── Patient table ── */}
      {!loading && (
        <div className="card border-0 shadow-sm">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th>#</th>
                  <th>Patient No.</th>
                  <th>Full Name</th>
                  <th>Date of Birth</th>
                  <th>Gender</th>
                  <th>Medicare No.</th>
                  <th>Phone</th>
                  <th>Status</th>
                  <th className="text-end">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr>
                    <td colSpan={9} className="text-center py-5 text-muted">
                      {search ? 'No patients match your search.' : 'No patients registered yet.'}
                    </td>
                  </tr>
                ) : (
                  filtered.map((patient, idx) => (
                    <tr key={patient.patientId}>
                      <td className="text-muted small">{idx + 1}</td>
                      <td>
                        <span className="badge bg-light text-dark border fw-normal">
                          {patient.patientNumber || '—'}
                        </span>
                      </td>
                      <td className="fw-semibold">
                        {[patient.firstName, patient.middleName, patient.lastName]
                          .filter(Boolean)
                          .join(' ')}
                      </td>
                      <td>{formatDob(patient.dateOfBirth)}</td>
                      <td>{genderLabel[patient.gender] || patient.gender || '—'}</td>
                      <td className="text-muted small">
                        {patient.medicareNumber || '—'}
                      </td>
                      <td className="text-muted small">{patient.phone || '—'}</td>
                      <td>{statusBadge(patient.status)}</td>
                      <td className="text-end">
                        <div className="d-flex justify-content-end gap-1">
                          {/* View */}
                          <button
                            className="btn btn-sm btn-outline-primary"
                            title="View details"
                            onClick={() => handleView(patient)}
                          >
                            View
                          </button>
                          {/* Edit */}
                          <button
                            className="btn btn-sm btn-outline-secondary"
                            title="Edit patient"
                            onClick={() => handleEdit(patient)}
                          >
                            Edit
                          </button>
                          {/* Delete */}
                          <button
                            className="btn btn-sm btn-outline-danger"
                            title="Delete patient"
                            disabled={deleteLoading && deletingId === patient.patientId}
                            onClick={() => handleDeleteConfirm(patient.patientId)}
                          >
                            {deleteLoading && deletingId === patient.patientId ? (
                              <span className="spinner-border spinner-border-sm" />
                            ) : (
                              'Delete'
                            )}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* ── Delete confirmation modal ── */}
      {confirmId && (
        <>
          <div
            className="modal fade show d-block"
            tabIndex="-1"
            role="dialog"
            style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}
          >
            <div className="modal-dialog modal-dialog-centered" role="document">
              <div className="modal-content border-0 shadow">
                <div className="modal-header border-0">
                  <h5 className="modal-title fw-bold">Confirm Delete</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setConfirmId(null)}
                  />
                </div>
                <div className="modal-body">
                  <p className="mb-0">
                    Are you sure you want to delete this patient? This action
                    cannot be undone.
                  </p>
                </div>
                <div className="modal-footer border-0">
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={() => setConfirmId(null)}
                  >
                    Cancel
                  </button>
                  <button
                    type="button"
                    className="btn btn-danger"
                    onClick={handleDeleteExecute}
                  >
                    Delete Patient
                  </button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
