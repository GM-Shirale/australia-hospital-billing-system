import React from 'react';

const UseFormLabelInput = ({
  title,
  field,
  register,
  error,
  disable = false,
  required = false,
  type = 'text',
  placeholder = '',
}) => {
  return (
    <div>
      <label className="form-label">
        {title}
        {required && <span className="text-danger ms-1">*</span>}
      </label>

      <input
        type={type}
        className={`form-control ${error ? 'is-invalid' : ''}`}
        placeholder={placeholder}
        disabled={disable}
        {...register(field)}
      />

      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
};

export default UseFormLabelInput;
