export const patientFields = [
  {
    field: "firstName",
    title: "First Name",
    type: "text",
    required: true,
    placeholder: "Enter first name",
    className: "col-12 col-md-4",
  },

  {
    field: "middleName",
    title: "Middle Name",
    type: "text",
    placeholder: "Enter middle name",
    className: "col-12 col-md-4",
  },

  {
    field: "lastName",
    title: "Last Name",
    type: "text",
    required: true,
    placeholder: "Enter last name",
    className: "col-12 col-md-4",
  },

  {
    field: "dateOfBirth",
    title: "Date of Birth",
    type: "date",
    required: true,
    className: "col-12 col-md-4",
  },

  {
    field: "gender",
    title: "Gender",
    type: "select",
    required: true,
    className: "col-12 col-md-4",
    options: [
      {
        value: "MALE",
        label: "Male",
      },
      {
        value: "FEMALE",
        label: "Female",
      },
      {
        value: "OTHER",
        label: "Other",
      },
      {
        value: "NOT_SPECIFIED",
        label: "Not Specified",
      },
    ],
  },

  {
    field: "medicareNumber",
    title: "Medicare Number",
    type: "text",
    placeholder: "Enter Medicare number",
    className: "col-12 col-md-4",
  },

  {
    field: "medicareIrn",
    title: "Medicare IRN",
    type: "text",
    placeholder: "Enter Medicare IRN",
    className: "col-12 col-md-4",
  },

  {
    field: "email",
    title: "Email",
    type: "text",
    placeholder: "Enter email",
    className: "col-12 col-md-4",
  },

  {
    field: "phone",
    title: "Phone",
    type: "text",
    placeholder: "Enter phone number",
    className: "col-12 col-md-4",
  },

  {
    field: "emergencyContactName",
    title: "Emergency Contact Name",
    type: "text",
    placeholder: "Enter contact name",
    className: "col-12 col-md-4",
  },

  {
    field: "emergencyContactPhone",
    title: "Emergency Contact Phone",
    type: "text",
    placeholder: "Enter contact phone",
    className: "col-12 col-md-4",
  },
];
