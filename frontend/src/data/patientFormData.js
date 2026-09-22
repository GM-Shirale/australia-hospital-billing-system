export const patientFormData = [
  {
    field: "patientName",
    title: "Patient Name",
    type: "text",
    className: "col-md-6",
  },

  {
    field: "age",
    title: "Age",
    type: "number",
    className: "col-md-6",
  },

  {
    field: "gender",
    title: "Gender",
    type: "select",
    className: "col-md-6",

    options: [
      {
        label: "Male",
        value: "MALE",
      },
      {
        label: "Female",
        value: "FEMALE",
      },
      {
        label: "Other",
        value: "OTHER",
      },
    ],
  },

  {
    field: "mobileNumber",
    title: "Mobile Number",
    type: "text",
    className: "col-md-6",
  },

  {
    field: "email",
    title: "Email",
    type: "text",
    className: "col-md-6",
  },

  {
    field: "bloodGroup",
    title: "Blood Group",
    type: "select",
    className: "col-md-6",

    options: [
      { label: "A+", value: "A+" },
      { label: "A-", value: "A-" },
      { label: "B+", value: "B+" },
      { label: "B-", value: "B-" },
      { label: "AB+", value: "AB+" },
      { label: "AB-", value: "AB-" },
      { label: "O+", value: "O+" },
      { label: "O-", value: "O-" },
    ],
  },
];