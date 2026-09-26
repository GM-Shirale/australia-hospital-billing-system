import React from 'react';

const UseFormSelect = ({
  title,
  field,
  register,
  options = [],
  error,
  disable = false,
  required = false,
}) => {
  return (
    <div>
      <label className="form-label">
        {title}
        {required && <span className="text-danger ms-1">*</span>}
      </label>

      <select
        className={`form-select ${error ? 'is-invalid' : ''}`}
        disabled={disable}
        {...register(field)}
      >
        <option value="">-- Select {title} --</option>
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
