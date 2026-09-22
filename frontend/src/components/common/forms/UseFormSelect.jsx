import React from "react";

const UseFormSelect = ({
  title,
  field,
  register,
  options = [],
  error,
  disable = false,
}) => {
  return (
    <div>
      <label className="form-label">{title}</label>

      <select
        className={`form-select ${error ? "is-invalid" : ""}`}
        {...register(field)}
        disabled={disable}
      >
        <option value="">Select {title}</option>

        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>

      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
};

export default UseFormSelect;
