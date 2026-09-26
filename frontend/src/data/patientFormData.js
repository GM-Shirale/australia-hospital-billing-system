// ─── Patient form field definitions ─────────────────────────────────────────
// Used by FormBox → InputBox → UseFormLabelInput / UseFormSelect / UseDate
// Field names MUST match PatientRequest Java DTO exactly.

export const patientNameFields = [
  {
    field: 'firstName',
    title: 'First Name',
    type: 'text',
    className: 'col-md-4',
    required: true,
  },
  {
    field: 'middleName',
    title: 'Middle Name',
    type: 'text',
    className: 'col-md-4',
  },
  {
    field: 'lastName',
    title: 'Last Name',
    type: 'text',
    className: 'col-md-4',
    required: true,
  },
];

export const patientDemographicFields = [
  {
    field: 'dateOfBirth',
    title: 'Date of Birth',
    type: 'date',
    className: 'col-md-4',
    required: true,
    maxDate: new Date(),
  },
  {
    field: 'gender',
    title: 'Gender',
    type: 'select',
    className: 'col-md-4',
    required: true,
    // Matches backend Gender enum exactly: MALE | FEMALE | OTHER | NOT_SPECIFIED
    options: [
      { label: 'Male', value: 'MALE' },
      { label: 'Female', value: 'FEMALE' },
      { label: 'Other', value: 'OTHER' },
      { label: 'Not Specified', value: 'NOT_SPECIFIED' },
    ],
  },
];

export const patientMedicareFields = [
  {
    field: 'medicareNumber',
    title: 'Medicare Number',
    type: 'text',
    className: 'col-md-4',
  },
  {
    field: 'medicareIrn',
    title: 'Medicare IRN',
    type: 'text',
    className: 'col-md-4',
  },
];

export const patientContactFields = [
  {
    field: 'email',
    title: 'Email',
    type: 'text',
    className: 'col-md-6',
  },
  {
    field: 'phone',
    title: 'Phone',
    type: 'text',
    className: 'col-md-6',
  },
];

export const patientEmergencyFields = [
  {
    field: 'emergencyContactName',
    title: 'Emergency Contact Name',
    type: 'text',
    className: 'col-md-6',
  },
  {
    field: 'emergencyContactPhone',
    title: 'Emergency Contact Phone',
    type: 'text',
    className: 'col-md-6',
  },
];
