// frontend/src/pages/users/userValidation.js
// Yup validation schema for UserForm — matches the User entity constraints

import * as yup from 'yup';

export const userValidationSchema = yup.object({
  username: yup
    .string()
    .min(3, 'Username must be at least 3 characters')
    .required('Username is required'),

  email: yup
    .string()
    .email('Invalid email format')
    .required('Email is required'),

  // Password required on create; optional on edit (leave blank = keep existing)
  password: yup
    .string()
    .when('$isEdit', {
      is: true,
      then: (s) => s.min(6, 'Password must be at least 6 characters').nullable().transform((v) => v || null),
      otherwise: (s) => s.min(6, 'Password must be at least 6 characters').required('Password is required'),
    }),

  confirmPassword: yup
    .string()
    .oneOf([yup.ref('password'), null, ''], 'Passwords do not match'),

  role: yup
    .string()
    .oneOf(
      ['DOCTOR', 'BILLING_STAFF', 'LAB_STAFF', 'PHARMACY_STAFF', 'RECEPTIONIST'],
      'Please select a role'
    )
    .required('Role is required'),

  active: yup.string().required('Please select an account status'),
});

export const userDefaultValues = {
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  role: '',
  active: 'true',
};
