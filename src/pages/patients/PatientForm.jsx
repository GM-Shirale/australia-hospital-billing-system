// frontend/src/pages/patients/PatientForm.jsx

import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import FormBox from '../../components/common/forms/FormBox';
import {
  createPatient,
  updatePatient,
  fetchPatientById,
  clearMessages,
} from '../../store/patientSlice';

import {
  patientNameFields,
  patientDemographicFields,
  patientMedicareFields,
  patientContactFields,
  patientEmergencyFields,
} from '../../data/patientFormData';

import { patientValidationSchema, patientDefaultValues } from './patientValidation';

export default function PatientForm() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { patientId } = useParams();
  const isEdit = Boolean(patientId);

  const { selectedPatient, formLoading, error, successMessage } = useSelector(
    (state) => state.patient
  );

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(patientValidationSchema),
    defaultValues: patientDefaultValues,
  });

  // ── On edit mode: load the patient data
  useEffect(() => {
    if (isEdit) {
      dispatch(fetchPatientById(patientId));
    }
  }, [isEdit, patientId, dispatch]);

  // ── Populate form once the patient is loaded
  useEffect(() => {
    if (
      isEdit &&
      selectedPatient &&
      String(selectedPatient.patientId) === String(patientId)
    ) {
      reset({
        firstName:             selectedPatient.firstName             || '',
        middleName:            selectedPatient.middleName            || '',
        lastName:              selectedPatient.lastName              || '',
        dateOfBirth:           selectedPatient.dateOfBirth           || '',
        gender:                selectedPatient.gender                || '',
        medicareNumber:        selectedPatient.medicareNumber        || '',
        medicareIrn:           selectedPatient.medicareIrn           || '',
        email:                 selectedPatient.email                 || '',
        phone:                 selectedPatient.phone                 || '',
        emergencyContactName:  selectedPatient.emergencyContactName  || '',
        emergencyContactPhone: selectedPatient.emergencyContactPhone || '',
      });
    }
  }, [selectedPatient, isEdit, patientId, reset]);

  // ── Redirect to list after a successful save
  useEffect(() => {
    if (successMessage) {
      const timer = setTimeout(() => {
        dispatch(clearMessages());
        navigate('/patients');
      }, 1200);
      return () => clearTimeout(timer);
    }
  }, [successMessage, dispatch, navigate]);

  // ── Clear messages on unmount
  useEffect(() => {
    return () => { dispatch(clearMessages()); };
  }, [dispatch]);

  // ── Submit handler
  const onSubmit = (formData) => {
    const payload = {
      firstName:             formData.firstName,
      middleName:            formData.middleName            || null,
      lastName:              formData.lastName,
      dateOfBirth:           formData.dateOfBirth           || null,
      gender:                formData.gender,
      medicareNumber:        formData.medicareNumber        || null,
      medicareIrn:           formData.medicareIrn           || null,
      email:                 formData.email                 || null,
      phone:                 formData.phone                 || null,
      emergencyContactName:  formData.emergencyContactName  || null,
      emergencyContactPhone: formData.emergencyContactPhone || null,
    };

    if (isEdit) {
      dispatch(updatePatient({ patientId, data: payload }));
    } else {
      dispatch(createPatient(payload));
    }
  };

  const sharedProps = { register, watch, setValue, errors };

  return (
    <div style={{ maxWidth: 860, margin: '0 auto' }}>

      {/* ── Page header ── */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 20 }}>
        <button
          type="button"
          onClick={() => navigate('/patients')}
          style={{
            background: 'none', border: '1px solid #cbd5e1',
            borderRadius: 8, padding: '5px 12px',
            fontSize: 13, cursor: 'pointer', color: '#475569',
          }}
        >
          ← Back
        </button>
        <h5 style={{ margin: 0, fontWeight: 700, color: '#0f172a' }}>
          {isEdit ? 'Edit Patient' : 'Register New Patient'}
        </h5>
      </div>

      {/* ── Alerts ── */}
      {error && (
        <div className="alert alert-danger alert-dismissible" role="alert" style={{ marginBottom: 16 }}>
          {typeof error === 'string' ? error : 'An error occurred. Please try again.'}
          <button type="button" className="btn-close" onClick={() => dispatch(clearMessages())} />
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success" role="alert" style={{ marginBottom: 16 }}>
          {successMessage}
        </div>
      )}

      <form onSubmit={handleSubmit(onSubmit)} noValidate>

        <Section title="Patient Name">
          <FormBox options={patientNameFields} {...sharedProps} />
        </Section>

        <Section title="Demographics">
          <FormBox options={patientDemographicFields} {...sharedProps} />
        </Section>

        <Section title="Medicare Details">
          <FormBox options={patientMedicareFields} {...sharedProps} />
        </Section>

        <Section title="Contact Information">
          <FormBox options={patientContactFields} {...sharedProps} />
        </Section>

        <Section title="Emergency Contact" color="#475569">
          <FormBox options={patientEmergencyFields} {...sharedProps} />
        </Section>

        {/* ── Actions ── */}
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 10, paddingBottom: 32 }}>
          <button
            type="button"
            onClick={() => navigate('/patients')}
            disabled={formLoading}
            style={{
              padding: '8px 20px', borderRadius: 8,
              border: '1px solid #cbd5e1', background: '#fff',
              cursor: 'pointer', fontSize: 14, color: '#475569',
            }}
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={formLoading}
            style={{
              padding: '8px 28px', borderRadius: 8, border: 'none',
              background: '#2563eb', color: '#fff',
              cursor: formLoading ? 'not-allowed' : 'pointer',
              fontSize: 14, fontWeight: 600, opacity: formLoading ? 0.7 : 1,
            }}
          >
            {formLoading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" role="status" />
                {isEdit ? 'Updating...' : 'Registering...'}
              </>
            ) : isEdit ? 'Update Patient' : 'Register Patient'}
          </button>
        </div>
      </form>
    </div>
  );
}

// ── Section card helper ───────────────────────────────
function Section({ title, color = '#2563eb', children }) {
  return (
    <div style={{
      background: '#fff',
      border: '1px solid #e2e8f0',
      borderRadius: 10,
      marginBottom: 20,
      boxShadow: '0 1px 3px rgba(0,0,0,0.05)',
      overflow: 'hidden',
    }}>
      <div style={{
        background: color, color: '#fff',
        padding: '10px 20px', fontWeight: 600, fontSize: 14,
      }}>
        {title}
      </div>
      <div style={{ padding: '20px' }}>
        {children}
      </div>
    </div>
  );
}
