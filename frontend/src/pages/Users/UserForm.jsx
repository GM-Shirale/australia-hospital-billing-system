// frontend/src/pages/users/UserForm.jsx

import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import FormBox from '../../components/common/forms/FormBox';
import {
  createUser,
  updateUser,
  fetchUserById,
  clearUserMessages,
} from '../../store/userSlice';

import {
  userCredentialFields,
  userPasswordFields,
  userRoleFields,
} from '../../data/userFormData';

import { userValidationSchema, userDefaultValues } from './userValidation';

export default function UserForm() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { userId } = useParams();
  const isEdit = Boolean(userId);

  const { selectedUser, formLoading, error, successMessage } = useSelector((s) => s.user);

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(userValidationSchema),
    context: { isEdit },
    defaultValues: userDefaultValues,
  });

  // ── Load user on edit mode
  useEffect(() => {
    if (isEdit) dispatch(fetchUserById(userId));
  }, [isEdit, userId, dispatch]);

  // ── Populate form when user data arrives
  useEffect(() => {
    if (isEdit && selectedUser && String(selectedUser.userId) === String(userId)) {
      reset({
        username:        selectedUser.username  || '',
        email:           selectedUser.email     || '',
        password:        '',           // never pre-fill passwords
        confirmPassword: '',
        role:            selectedUser.role   || '',
        active:          selectedUser.active !== false ? 'true' : 'false',
      });
    }
  }, [selectedUser, isEdit, userId, reset]);

  // ── Redirect after successful save
  useEffect(() => {
    if (successMessage) {
      const t = setTimeout(() => {
        dispatch(clearUserMessages());
        navigate('/users');
      }, 1200);
      return () => clearTimeout(t);
    }
  }, [successMessage, dispatch, navigate]);

  // ── Clear messages on unmount
  useEffect(() => {
    return () => { dispatch(clearUserMessages()); };
  }, [dispatch]);

  // ── Submit handler
  const onSubmit = (formData) => {
    const payload = {
      username: formData.username,
      email:    formData.email,
      role:     formData.role,
      active:   formData.active === 'true',
    };

    // Only include password if provided (for edit, leave blank = keep existing)
    if (formData.password) {
      payload.password = formData.password;
    }

    if (isEdit) {
      dispatch(updateUser({ userId, data: payload }));
    } else {
      dispatch(createUser(payload));
    }
  };

  const sharedProps = { register, watch, setValue, errors };

  return (
    <div style={{ maxWidth: 860, margin: '0 auto' }}>

      {/* ── Page header ── */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 20 }}>
        <button
          type="button"
          onClick={() => navigate('/users')}
          style={{
            background: 'none', border: '1px solid #cbd5e1',
            borderRadius: 8, padding: '5px 12px',
            fontSize: 13, cursor: 'pointer', color: '#475569',
          }}
        >
          ← Back
        </button>
        <h5 style={{ margin: 0, fontWeight: 700, color: '#0f172a' }}>
          {isEdit ? 'Edit User' : 'Add New User'}
        </h5>
      </div>

      {/* ── Alerts ── */}
      {error && (
        <div className="alert alert-danger alert-dismissible" role="alert" style={{ marginBottom: 16 }}>
          {typeof error === 'string' ? error : 'An error occurred. Please try again.'}
          <button type="button" className="btn-close" onClick={() => dispatch(clearUserMessages())} />
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success" role="alert" style={{ marginBottom: 16 }}>
          {successMessage}
        </div>
      )}

      <form onSubmit={handleSubmit(onSubmit)} noValidate>

        <Section title="Account Information">
          <FormBox options={userCredentialFields} {...sharedProps} />
        </Section>

        <Section title="Password" color="#475569">
          {isEdit && (
            <p style={{ fontSize: 12, color: '#64748b', marginBottom: 12 }}>
              Leave the password fields blank to keep the existing password.
            </p>
          )}
          <FormBox options={userPasswordFields} {...sharedProps} />
        </Section>

        <Section title="Role & Status">
          <FormBox options={userRoleFields} {...sharedProps} />
        </Section>

        {/* ── Actions ── */}
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 10, paddingBottom: 32 }}>
          <button
            type="button"
            onClick={() => navigate('/users')}
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
                {isEdit ? 'Updating...' : 'Creating...'}
              </>
            ) : isEdit ? 'Update User' : 'Create User'}
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
