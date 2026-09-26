// frontend/src/pages/patients/patientValidation.js
// Yup validation schema for PatientForm — matches PatientRequest Java DTO

import * as yup from 'yup';

export const patientValidationSchema = yup.object({
  firstName: yup.string().required('First name is required'),
  middleName: yup.string().nullable(),
  lastName: yup.string().required('Last name is required'),

  // UseDate sets value as yyyy-MM-dd string; yup coerces it to Date
  dateOfBirth: yup
    .date()
    .typeError('Date of birth is required')
    .required('Date of birth is required')
    .max(new Date(), 'Date of birth must be in the past'),

  gender: yup
    .string()
    .oneOf(
      ['MALE', 'FEMALE', 'OTHER', 'NOT_SPECIFIED'],
      'Please select a gender'
    )
    .required('Gender is required'),

  medicareNumber: yup.string().nullable(),
  medicareIrn: yup.string().nullable(),
  email: yup.string().email('Invalid email format').nullable(),
  phone: yup.string().nullable(),
  emergencyContactName: yup.string().nullable(),
  emergencyContactPhone: yup.string().nullable(),
});

export const patientDefaultValues = {
  firstName: '',
  middleName: '',
  lastName: '',
  dateOfBirth: '',
  gender: '',
  medicareNumber: '',
  medicareIrn: '',
  email: '',
  phone: '',
  emergencyContactName: '',
  emergencyContactPhone: '',
};
