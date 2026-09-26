import * as yup from "yup";

export const patientValidationSchema = yup.object({
  firstName: yup
    .string()
    .trim()
    .required("First name is required"),

  middleName: yup
    .string()
    .nullable(),

  lastName: yup
    .string()
    .trim()
    .required("Last name is required"),

  dateOfBirth: yup
    .date()
    .typeError("Date of birth is required")
    .required("Date of birth is required")
    .max(new Date(), "Date of birth must be in the past"),

  gender: yup
    .string()
    .oneOf(
      ["MALE", "FEMALE", "OTHER", "NOT_SPECIFIED"],
      "Please select a valid gender"
    )
    .required("Gender is required"),

  medicareNumber: yup
    .string()
    .nullable(),

  medicareIrn: yup
    .string()
    .nullable(),

  email: yup
    .string()
    .email("Invalid email format")
    .nullable(),

  phone: yup
    .string()
    .nullable(),

  emergencyContactName: yup
    .string()
    .nullable(),

  emergencyContactPhone: yup
    .string()
    .nullable(),
});

