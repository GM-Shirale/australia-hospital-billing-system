export const patientFormData = [
  {
    field: "firstName",
    title: "First Name",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "middleName",
    title: "Middle Name",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "lastName",
    title: "Last Name",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "dateOfBirth",
    title: "Date of Birth",
    type: "date",
    className: "col-md-4",
  },
  {
    field: "gender",
    title: "Gender",
    type: "select",
    className: "col-md-4",
    options: [
      { label: "Male", value: "MALE" },
      { label: "Female", value: "FEMALE" },
      { label: "Other", value: "OTHER" },
    ],
  },
  {
    field: "medicareNumber",
    title: "Medicare Number",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "medicareIrn",
    title: "Medicare IRN",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "email",
    title: "Email",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "phone",
    title: "Phone",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "emergencyContactName",
    title: "Emergency Contact Name",
    type: "text",
    className: "col-md-4",
  },
  {
    field: "emergencyContactPhone",
    title: "Emergency Contact Phone",
    type: "text",
    className: "col-md-4",
  },
];