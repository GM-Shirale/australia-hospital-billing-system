// frontend/src/data/userFormData.js
// Form field definitions for UserForm — matches the User entity and Role enum.
// Role enum values: DOCTOR | BILLING_STAFF | LAB_STAFF | PHARMACY_STAFF | RECEPTIONIST

export const userCredentialFields = [
  {
    field: 'username',
    title: 'Username',
    type: 'text',
    className: 'col-md-6',
    required: true,
    placeholder: 'e.g. john.doe',
  },
  {
    field: 'email',
    title: 'Email Address',
    type: 'text',
    className: 'col-md-6',
    required: true,
    placeholder: 'e.g. john.doe@hospital.com',
  },
];

export const userPasswordFields = [
  {
    field: 'password',
    title: 'Password',
    type: 'password',
    className: 'col-md-6',
    required: true,
    placeholder: 'Minimum 6 characters',
  },
  {
    field: 'confirmPassword',
    title: 'Confirm Password',
    type: 'password',
    className: 'col-md-6',
    required: true,
    placeholder: 'Re-enter password',
  },
];

export const userRoleFields = [
  {
    field: 'role',
    title: 'Staff Role',
    type: 'select',
    className: 'col-md-6',
    required: true,
    options: [
      { label: 'Doctor',            value: 'DOCTOR' },
      { label: 'Billing Staff',     value: 'BILLING_STAFF' },
      { label: 'Lab Staff',         value: 'LAB_STAFF' },
      { label: 'Pharmacy Staff',    value: 'PHARMACY_STAFF' },
      { label: 'Receptionist',      value: 'RECEPTIONIST' },
    ],
  },
  {
    field: 'active',
    title: 'Account Status',
    type: 'select',
    className: 'col-md-6',
    required: true,
    options: [
      { label: 'Active',   value: 'true' },
      { label: 'Inactive', value: 'false' },
    ],
  },
];
