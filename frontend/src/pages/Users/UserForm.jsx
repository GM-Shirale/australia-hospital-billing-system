import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";

import FormBox from "../../components/common/forms/FormBox";
import {
  userCredentialFields,
  userPasswordFields,
  userRoleFields,
} from "../../data/userFormData";
import {
  createUser,
  updateUser,
  fetchUserById,
  clearUserError,
  clearUserSuccess,
  clearSelectedUser,
} from "../../store/userSlice";

// =============================================
// Validation schema
// =============================================
const createSchema = yup.object({
  username: yup
    .string()
    .required("Username is required")
    .min(3, "Minimum 3 characters")
    .max(50, "Maximum 50 characters"),
  email: yup
    .string()
    .required("Email is required")
    .email("Enter a valid email"),
  password: yup
    .string()
    .required("Password is required")
    .min(6, "Minimum 6 characters"),
  confirmPassword: yup
    .string()
    .required("Please confirm password")
    .oneOf([yup.ref("password")], "Passwords do not match"),
  role: yup.string().required("Role is required"),
  active: yup.string().required("Status is required"),
});

const editSchema = yup.object({
  username: yup
    .string()
    .required("Username is required")
    .min(3, "Minimum 3 characters")
    .max(50, "Maximum 50 characters"),
  email: yup
    .string()
    .required("Email is required")
    .email("Enter a valid email"),
  password: yup.string().nullable().transform((v) => v || undefined),
  confirmPassword: yup
    .string()
    .nullable()
    .when("password", {
      is: (val) => val && val.length > 0,
      then: (schema) =>
        schema
          .required("Please confirm password")
          .oneOf([yup.ref("password")], "Passwords do not match"),
    }),
  role: yup.string().required("Role is required"),
  active: yup.string().required("Status is required"),
});

// =============================================
// UserForm component
// =============================================
const UserForm = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { userId } = useParams();

  const isEdit = !!userId;

  const { loading, error, successMessage, selectedUser } = useSelector(
    (s) => s.user
  );

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    reset,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(isEdit ? editSchema : createSchema),
    defaultValues: {
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
      role: "",
      active: "true",
    },
  });

  // Load user data when editing
  useEffect(() => {
    if (isEdit) {
      dispatch(fetchUserById(Number(userId)));
    }
    return () => {
      dispatch(clearSelectedUser());
    };
  }, [dispatch, isEdit, userId]);

  // Populate form when selectedUser loads
  useEffect(() => {
    if (isEdit && selectedUser) {
      reset({
        username: selectedUser.username || "",
        email: selectedUser.email || "",
        password: "",
        confirmPassword: "",
        role: selectedUser.role || "",
        active: String(selectedUser.active ?? "true"),
      });
    }
  }, [selectedUser, isEdit, reset]);

  // Navigate on success
  useEffect(() => {
    if (successMessage) {
      dispatch(clearUserSuccess());
      navigate("/users");
    }
  }, [successMessage, dispatch, navigate]);

  // Clear error on unmount
  useEffect(() => {
    return () => {
      dispatch(clearUserError());
    };
  }, [dispatch]);

  // ---- submit ----
  const onSubmit = (data) => {
    const payload = {
      username: data.username,
      email: data.email,
      role: data.role,
      active: data.active === "true",
    };

    if (data.password) {
      payload.password = data.password;
    }

    if (isEdit) {
      dispatch(updateUser({ userId: Number(userId), data: payload }));
    } else {
      payload.password = data.password; // required for create
      dispatch(createUser(payload));
    }
  };

  // ---- section-level error helper (for FormBox) ----
  const sectionErrors = (fields) => {
    const out = {};
    fields.forEach((f) => {
      if (errors[f.field]) out[f.field] = errors[f.field];
    });
    return out;
  };

  return (
    <div className="container py-4" style={{ maxWidth: 800 }}>
      {/* Breadcrumb */}
      <nav aria-label="breadcrumb" className="mb-3">
        <ol className="breadcrumb">
          <li className="breadcrumb-item">
            <button
              className="btn btn-link p-0 text-decoration-none"
              onClick={() => navigate("/users")}
            >
              User Management
            </button>
          </li>
          <li className="breadcrumb-item active">
            {isEdit ? "Edit User" : "Register New User"}
          </li>
        </ol>
      </nav>

      <h4 className="fw-bold mb-4">
        {isEdit ? "Edit User" : "Register New User"}
      </h4>

      {/* Error alert */}
      {error && (
        <div className="alert alert-danger alert-dismissible" role="alert">
          {error}
          <button
            type="button"
            className="btn-close"
            onClick={() => dispatch(clearUserError())}
          />
        </div>
      )}

      <form onSubmit={handleSubmit(onSubmit)} noValidate>

        {/* ---- Section 1: Account Details ---- */}
        <div className="card border-0 shadow-sm mb-4">
          <div className="card-header bg-white fw-semibold py-3">
            Account Details
          </div>
          <div className="card-body">
            <FormBox
              options={userCredentialFields}
              register={register}
              watch={watch}
              setValue={setValue}
              errors={errors}
            />
          </div>
        </div>

        {/* ---- Section 2: Password ---- */}
        <div className="card border-0 shadow-sm mb-4">
          <div className="card-header bg-white fw-semibold py-3">
            {isEdit
              ? "Change Password (leave blank to keep current)"
              : "Set Password"}
          </div>
          <div className="card-body">
            <FormBox
              options={userPasswordFields}
              register={register}
              watch={watch}
              setValue={setValue}
              errors={errors}
            />
          </div>
        </div>

        {/* ---- Section 3: Role & Status ---- */}
        <div className="card border-0 shadow-sm mb-4">
          <div className="card-header bg-white fw-semibold py-3">
            Role & Status
          </div>
          <div className="card-body">
            <FormBox
              options={userRoleFields}
              register={register}
              watch={watch}
              setValue={setValue}
              errors={errors}
            />
          </div>
        </div>

        {/* ---- Actions ---- */}
        <div className="d-flex justify-content-end gap-2">
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => navigate("/users")}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? (
              <>
                <span
                  className="spinner-border spinner-border-sm me-2"
                  role="status"
                />
                {isEdit ? "Saving..." : "Registering..."}
              </>
            ) : isEdit ? (
              "Save Changes"
            ) : (
              "Register User"
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

export default UserForm;
