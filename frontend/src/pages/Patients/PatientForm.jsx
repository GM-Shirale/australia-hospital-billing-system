import React from "react";
import { useForm } from "react-hook-form";

import FormBox from "../../components/common/forms/FormBox";
import { patientFormData } from "../../data/patientFormData";

const PatientForm = () => {
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    console.log("Patient Data:", data);
  };

  return (
    <div className="container-fluid p-4">
      <div className="card shadow-sm col-6">
        <div className="card-header">
          <h4 className="mb-0">Patient Registration</h4>
        </div>

        <div className="card-body">
          <form onSubmit={handleSubmit(onSubmit)}>
            <FormBox
              options={patientFormData}
              register={register}
              watch={watch}
              setValue={setValue}
              errors={errors}
            />

            <div className="d-flex justify-content-end mt-4">
              <button type="submit" className="btn btn-primary">
                Save Patient
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PatientForm;
