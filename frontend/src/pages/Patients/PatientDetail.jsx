import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import { format, parseISO, isValid } from 'date-fns';

import {
  fetchPatientById,
  deletePatient,
  clearMessages,
} from '../../store/patientSlice';

// ── Helpers ──────────────────────────────────────────────────────────────────

const fmt = (val) => val || '—';

const formatDob = (dob) => {
  if (!dob) return '—';
  const d = parseISO(dob);
  return isValid(d) ? format(d, 'dd/MM/yyyy') : '—';
};

const formatDateTime = (dt) => {
  if (!dt) return '—';
  const d = new Date(dt);
  return isValid(d) ? format(d, 'dd/MM/yyyy HH:mm') : '—';
};

const statusBadge = (status) => {
  const map = { ACTIVE: 'success', INACTIVE: 'secondary', DECEASED: 'dark' };
  return (
    <span className={`badge bg-${map[status] || 'secondary'} fs-6`}>
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

// ── Detail row component ─────────────────────────────────────────────────────
const DetailRow = ({ label, value }) => (
  <div className="col-md-6 mb-3">
    <p className="text-muted small mb-1 fw-semibold text-uppercase" style={{ fontSize: '0.7rem', letterSpacing: '0.05em' }}>
      {label}
    </p>
    <p className="mb-0 fw-semibold">{value || '—'}</p>
  </div>
);

// ─────────────────────────────────────────────────────────────────────────────

export default function PatientDetail() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { patientId } = useParams();

  const { selectedPatient, loading, deleteLoading, error } = useSelector(
    (state) => state.patient
  );

  const [showConfirm, setShowConfirm] = useState(false);

  useEffect(() => {
    // Always fetch fresh from backend when landing on this page
    dispatch(fetchPatientById(patientId));
  }, [patientId, dispatch]);

  useEffect(() => {
    return () => {
      dispatch(clearMessages());
    };
  }, [dispatch]);

  const handleDelete = async () => {
    setShowConfirm(false);
    const result = await dispatch(deletePatient(patientId));
    if (!result.error) {
      navigate('/patients');
    }
  };

  // ── Loading state ──
  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status" />
        <p className="text-muted mt-2">Loading patient details...</p>
      </div>
    );
  }

  // ── Error state ──
  if (error && !selectedPatient) {
    return (
      <div className="container-fluid py-4">
        <div className="alert alert-danger">{error}</div>
        <button className="btn btn-outline-secondary" onClick={() => navigate('/patients')}>
          ← Back to Patients
        </button>
      </div>
    );
  }

  if (!selectedPatient) return null;

  const fullName = [
    selectedPatient.firstName,
    selectedPatient.middleName,
    selectedPatient.lastName,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className="container-fluid py-4">
      {/* ── Page header ── */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="d-flex align-items-center gap-3">
          <button
            className="btn btn-outline-secondary btn-sm"
            onClick={() => navigate('/patients')}
          >
            ← Back
          </button>
          <div>
            <h4 className="mb-0 fw-bold">{fullName}</h4>
            <small className="text-muted">{selectedPatient.patientNumber}</small>
          </div>
        </div>
        <div className="d-flex gap-2">
          <button
            className="btn btn-outline-secondary"
            onClick={() => navigate(`/patients/${patientId}/edit`)}
          >
            Edit Patient
          </button>
          <button
            className="btn btn-outline-danger"
            onClick={() => setShowConfirm(true)}
            disabled={deleteLoading}
          >
            {deleteLoading ? (
              <span className="spinner-border spinner-border-sm" />
            ) : (
              'Delete Patient'
            )}
          </button>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger mb-3">{error}</div>
      )}

      {/* ══ Card 1 — Identity ══ */}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-primary text-white py-3 d-flex justify-content-between align-items-center">
          <h6 className="mb-0 fw-semibold">Patient Identity</h6>
          {statusBadge(selectedPatient.status)}
        </div>
        <div className="card-body p-4">
          <div className="row">
            <DetailRow label="Patient Number" value={selectedPatient.patientNumber} />
            <DetailRow label="First Name" value={selectedPatient.firstName} />
            <DetailRow label="Middle Name" value={selectedPatient.middleName} />
            <DetailRow label="Last Name" value={selectedPatient.lastName} />
            <DetailRow label="Date of Birth" value={formatDob(selectedPatient.dateOfBirth)} />
            <DetailRow label="Gender" value={genderLabel[selectedPatient.gender] || selectedPatient.gender} />
          </div>
        </div>
      </div>

      {/* ══ Card 2 — Medicare ══ */}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-primary text-white py-3">
          <h6 className="mb-0 fw-semibold">Medicare Details</h6>
        </div>
        <div className="card-body p-4">
          <div className="row">
            <DetailRow label="Medicare Number" value={selectedPatient.medicareNumber} />
            <DetailRow label="Medicare IRN" value={selectedPatient.medicareIrn} />
          </div>
        </div>
      </div>

      {/* ══ Card 3 — Contact ══ */}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-primary text-white py-3">
          <h6 className="mb-0 fw-semibold">Contact Information</h6>
        </div>
        <div className="card-body p-4">
          <div className="row">
            <DetailRow label="Email" value={selectedPatient.email} />
            <DetailRow label="Phone" value={selectedPatient.phone} />
          </div>
        </div>
      </div>

      {/* ══ Card 4 — Emergency Contact ══ */}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-secondary text-white py-3">
          <h6 className="mb-0 fw-semibold">Emergency Contact</h6>
        </div>
        <div className="card-body p-4">
          <div className="row">
            <DetailRow label="Contact Name" value={selectedPatient.emergencyContactName} />
            <DetailRow label="Contact Phone" value={selectedPatient.emergencyContactPhone} />
          </div>
        </div>
      </div>

      {/* ══ Card 5 — Audit ══ */}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-header bg-light py-3">
          <h6 className="mb-0 fw-semibold text-muted">Record Audit</h6>
        </div>
        <div className="card-body p-4">
          <div className="row">
            <DetailRow label="Created At" value={formatDateTime(selectedPatient.createdAt)} />
            <DetailRow label="Last Updated" value={formatDateTime(selectedPatient.updatedAt)} />
          </div>
        </div>
      </div>

      {/* ── Delete confirmation modal ── */}
      {showConfirm && (
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
                  onClick={() => setShowConfirm(false)}
                />
              </div>
              <div className="modal-body">
                <p className="mb-0">
                  Are you sure you want to delete{' '}
                  <strong>{fullName}</strong>? This action cannot be undone.
                </p>
              </div>
              <div className="modal-footer border-0">
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => setShowConfirm(false)}
                >
                  Cancel
                </button>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={handleDelete}
                >
                  Delete Patient
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
