import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { ShieldCheck, Lock, User } from 'lucide-react';

import { adminLogin, userLogin, clearAuthError } from '../../store/authSlice';

export default function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { isAuthenticated, loading, error } = useSelector((state) => state.auth);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loginType, setLoginType] = useState('ADMIN'); // ADMIN | USER

  // If already logged in, go straight to patients
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/patients', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Clear any stale error when the user switches login type
  useEffect(() => {
    dispatch(clearAuthError());
  }, [loginType, dispatch]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const credentials = { username, password };
    if (loginType === 'ADMIN') {
      dispatch(adminLogin(credentials));
    } else {
      dispatch(userLogin(credentials));
    }
  };

  return (
    <div
      className="d-flex align-items-center justify-content-center min-vh-100"
      style={{ background: '#f1f5f9' }}
    >
      <div
        className="bg-white rounded-4 p-5 shadow"
        style={{ width: '100%', maxWidth: 420 }}
      >
        {/* ── Logo / title ── */}
        <div className="text-center mb-4">
          <div
            className="d-inline-flex align-items-center justify-content-center rounded-3 mb-3"
            style={{ width: 56, height: 56, background: '#eff6ff' }}
          >
            <ShieldCheck size={28} color="#2563eb" />
          </div>
          <h4 className="fw-black mb-1">Hospital Staff Portal</h4>
          <p className="text-muted small mb-0">
            Sign in to access MBS Clinical &amp; Billing Services
          </p>
        </div>

        {/* ── Login type toggle ── */}
        <div className="d-flex mb-4 rounded-3 overflow-hidden border">
          <button
            type="button"
            className={`flex-fill py-2 border-0 fw-semibold small ${
              loginType === 'ADMIN'
                ? 'bg-primary text-white'
                : 'bg-white text-muted'
            }`}
            onClick={() => setLoginType('ADMIN')}
          >
            Admin Login
          </button>
          <button
            type="button"
            className={`flex-fill py-2 border-0 fw-semibold small ${
              loginType === 'USER'
                ? 'bg-primary text-white'
                : 'bg-white text-muted'
            }`}
            onClick={() => setLoginType('USER')}
          >
            Staff Login
          </button>
        </div>

        {/* ── Error alert ── */}
        {error && (
          <div className="alert alert-danger py-2 small mb-3" role="alert">
            {typeof error === 'string' ? error : 'Invalid username or password'}
          </div>
        )}

        {/* ── Form ── */}
        <form onSubmit={handleSubmit} noValidate>
          <div className="mb-3">
            <label className="form-label fw-semibold small text-uppercase text-muted">
              Username
            </label>
            <div className="input-group">
              <span className="input-group-text bg-light border-end-0">
                <User size={16} className="text-muted" />
              </span>
              <input
                type="text"
                className="form-control border-start-0"
                placeholder="Enter your username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                autoFocus
              />
            </div>
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold small text-uppercase text-muted">
              Password
            </label>
            <div className="input-group">
              <span className="input-group-text bg-light border-end-0">
                <Lock size={16} className="text-muted" />
              </span>
              <input
                type="password"
                className="form-control border-start-0"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <button
            type="submit"
            className="btn btn-primary w-100 fw-bold py-2"
            disabled={loading}
          >
            {loading ? (
              <>
                <span
                  className="spinner-border spinner-border-sm me-2"
                  role="status"
                  aria-hidden="true"
                />
                Signing in...
              </>
            ) : (
              'Sign In'
            )}
          </button>
        </form>
      </div>
    </div>
  );
}
