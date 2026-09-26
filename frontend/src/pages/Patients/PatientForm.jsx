import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";

import FormBox from "../../components/common/forms/FormBox";

import {
  createPatient,
  updatePatient,
  fetchPatientById,
  clearMessages,
} from "../../store/patientSlice";

import { patientFields } from "../../data/patientFormData";
import { patientValidationSchema } from "./patientValidation";

// ─────────────────────────────────────────────
// Default Values
// ─────────────────────────────────────────────

const defaultValues = {
  firstName: "",
  middleName: "",
  lastName: "",
  dateOfBirth: "",
  gender: "",
  medicareNumber: "",
  medicareIrn: "",
  email: "",
  phone: "",
  emergencyContactName: "",
  emergencyContactPhone: "",
};

// ─────────────────────────────────────────────
// Component
// ─────────────────────────────────────────────

export default function PatientForm() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { patientId } = useParams();
  const isEdit = Boolean(patientId);

  const {
    selectedPatient,
    formLoading,
    error,
    successMessage,
  } = useSelector((state) => state.patient);

  // ───────────────────────────────────────────
  // React Hook Form
  // ───────────────────────────────────────────

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(patientValidationSchema),
    defaultValues,
  });

  // ───────────────────────────────────────────
  // Fetch patient for edit
  // ───────────────────────────────────────────

  useEffect(() => {
    if (isEdit) {
      dispatch(fetchPatientById(patientId));
    }
  }, [isEdit, patientId, dispatch]);

  // ───────────────────────────────────────────
  // Populate form when patient is loaded
  // ───────────────────────────────────────────

  useEffect(() => {
    if (
      isEdit &&
      selectedPatient &&
      String(selectedPatient.patientId) === String(patientId)
    ) {
      reset({
        firstName: selectedPatient.firstName || "",
        middleName: selectedPatient.middleName || "",
        lastName: selectedPatient.lastName || "",
        dateOfBirth: selectedPatient.dateOfBirth || "",
        gender: selectedPatient.gender || "",
        medicareNumber: selectedPatient.medicareNumber || "",
        medicareIrn: selectedPatient.medicareIrn || "",
        email: selectedPatient.email || "",
        phone: selectedPatient.phone || "",
        emergencyContactName:
          selectedPatient.emergencyContactName || "",
        emergencyContactPhone:
          selectedPatient.emergencyContactPhone || "",
      });
    }
  }, [selectedPatient, isEdit, patientId, reset]);

  // ───────────────────────────────────────────
  // Success redirect
  // ───────────────────────────────────────────

  useEffect(() => {
    if (successMessage) {
      const timer = setTimeout(() => {
        dispatch(clearMessages());
        navigate("/patients");
      }, 1200);

      return () => clearTimeout(timer);
    }
  }, [successMessage, dispatch, navigate]);

  // ───────────────────────────────────────────
  // Cleanup
  // ───────────────────────────────────────────

  useEffect(() => {
    return () => {
      dispatch(clearMessages());
    };
  }, [dispatch]);

  // ───────────────────────────────────────────
  // Submit
  // ───────────────────────────────────────────

  const onSubmit = (formData) => {
    const payload = {
      firstName: formData.firstName,
      middleName: formData.middleName || null,
      lastName: formData.lastName,
      dateOfBirth: formData.dateOfBirth || null,
      gender: formData.gender,
      medicareNumber: formData.medicareNumber || null,
      medicareIrn: formData.medicareIrn || null,
      email: formData.email || null,
      phone: formData.phone || null,
      emergencyContactName:
        formData.emergencyContactName || null,
      emergencyContactPhone:
        formData.emergencyContactPhone || null,
    };

    if (isEdit) {
      dispatch(
        updatePatient({
          patientId,
          data: payload,
        })
      );
    } else {
      dispatch(createPatient(payload));
    }
  };

  return (
    <div className="container-fluid py-3">

      {/* Page Header */}

      {/* <div className="d-flex align-items-center gap-2 mb-3">
        <button
          type="button"
          className="btn btn-outline-secondary btn-sm"
          onClick={() => navigate("/patients")}
        >
          ← Back
        </button>

        <h5 className="mb-0">
          {isEdit ? "Edit Patient" : "Register New Patient"}
        </h5>
      </div> */}
      {/* Page Header */}

      <div className="d-flex align-items-center justify-content-between mb-4">
        <div className="d-flex align-items-center gap-3">
          <button
            type="button"
            className="btn btn-outline-secondary btn-sm"
            onClick={() => navigate("/patients")}
          >
            ← Back
          </button>

          <div>
            <h4 className="mb-1 fw-semibold">
              {isEdit ? "Edit Patient" : "Register New Patient"}
            </h4>

            <small className="text-muted">
              {isEdit
                ? "Update patient information"
                : "Enter patient information to register a new patient"}
            </small>
          </div>
        </div>
      </div>
      {/* Error */}

      {error && (
        <div
          className="alert alert-danger alert-dismissible"
          role="alert"
        >
          {typeof error === "string"
            ? error
            : "An error occurred. Please try again."}

          <button
            type="button"
            className="btn-close"
            aria-label="Close"
            onClick={() => dispatch(clearMessages())}
          />
        </div>
      )}

      {/* Success */}

      {successMessage && (
        <div className="alert alert-success" role="alert">
          {successMessage}
        </div>
      )}

      {/* Form */}

      <form
        onSubmit={handleSubmit(onSubmit)}
        noValidate
      >
        <FormBox
          options={patientFields}
          register={register}
          watch={watch}
          setValue={setValue}
          errors={errors}
        />

        {/* Actions */}

        <div className="d-flex justify-content-end gap-2 mt-3 mb-4">

          <button
            type="button"
            className="btn btn-outline-secondary btn-sm px-3"
            onClick={() => navigate("/patients")}
            disabled={formLoading}
          >
            Cancel
          </button>

          <button
            type="submit"
            className="btn btn-primary btn-sm px-4"
            disabled={formLoading}
          >
            {formLoading ? (
              <>
                <span
                  className="spinner-border spinner-border-sm me-2"
                  role="status"
                  aria-hidden="true"
                />

                {isEdit ? "Updating..." : "Registering..."}
              </>
            ) : (
              isEdit ? "Update Patient" : "Register Patient"
            )}
          </button>

        </div>
      </form>
    </div>
  );
}