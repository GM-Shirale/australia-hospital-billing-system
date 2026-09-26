// =============================================
// User Registration Form Field Definitions
// =============================================

export const userCredentialFields = [
  {
    field: "username",
    title: "Username",
    type: "text",
    className: "col-md-6",
  },
  {
    field: "email",
    title: "Email Address",
    type: "text",
    className: "col-md-6",
  },
];

export const userPasswordFields = [
  {
    field: "password",
    title: "Password",
    type: "text",
    className: "col-md-6",
  },
  {
    field: "confirmPassword",
    title: "Confirm Password",
    type: "text",
    className: "col-md-6",
  },
];

export const userRoleFields = [
  {
    field: "role",
    title: "Role",
    type: "select",
    className: "col-md-6",
    options: [
      { label: "Receptionist", value: "RECEPTIONIST" },
      { label: "Doctor", value: "DOCTOR" },
      { label: "Billing Staff", value: "BILLING_STAFF" },
      { label: "Lab Staff", value: "LAB_STAFF" },
      { label: "Pharmacy Staff", value: "PHARMACY_STAFF" },
    ],
  },
  {
    field: "active",
    title: "Status",
    type: "select",
    className: "col-md-6",
    options: [
      { label: "Active", value: "true" },
      { label: "Inactive", value: "false" },
    ],
  },
];
