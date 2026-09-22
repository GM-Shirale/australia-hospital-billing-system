import React from "react";

const UseFormLabelInput = ({
  title,
  field,
  register,
  error,
  disable = false,
  type = "text",
}) => {
  return (
    <div>
      <label className="form-label">
        {title}
      </label>

      <input
        type={type}
        className={`form-control ${
          error ? "is-invalid" : ""
        }`}
        {...register(field)}
        disabled={disable}
      />

      {error && (
        <div className="invalid-feedback">
          {error}
        </div>
      )}
    </div>
  );
};

export default UseFormLabelInput;