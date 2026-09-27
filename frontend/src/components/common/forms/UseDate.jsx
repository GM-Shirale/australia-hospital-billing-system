import React, { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { format, parseISO, isValid } from 'date-fns';

// ─── UseDate ─────────────────────────────────────────────────────────────────
// Integrates react-datepicker with React Hook Form via register + setValue.
// Display format : dd/MM/yyyy  (what the user sees)
// Form value     : yyyy-MM-dd  (string sent to Spring Boot LocalDate)
// ─────────────────────────────────────────────────────────────────────────────

const UseDate = ({
  title,
  field,
  register,
  setValue,
  watch,
  error,
  required = false,
  disable = false,
  minDate,
  maxDate,
}) => {
  const watchedValue = watch(field);

  // Parse the current string value (yyyy-MM-dd) back into a Date for the picker
  const parseWatched = (val) => {
    if (!val || val === 'undefined') return null;
    const parsed = parseISO(val);
    return isValid(parsed) ? parsed : null;
  };

  const [selectedDate, setSelectedDate] = useState(() =>
    parseWatched(watchedValue)
  );

  // Register the field manually (DatePicker is uncontrolled by RHF)
  useEffect(() => {
    register(field);
  }, [field, register]);

  // Sync when the form resets (e.g. edit mode loads patient data)
  useEffect(() => {
    setSelectedDate(parseWatched(watchedValue));
  }, [watchedValue]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleChange = (date) => {
    if (!date) {
      setSelectedDate(null);
      setValue(field, '', { shouldValidate: true });
      return;
    }
    setSelectedDate(date);
    // Send yyyy-MM-dd string so Spring Boot LocalDate deserialises correctly
    setValue(field, format(date, 'yyyy-MM-dd'), { shouldValidate: true });
  };

  return (
    <div>
      <label className="form-label">
        {title}
        {required && <span className="text-danger ms-1">*</span>}
      </label>

      <DatePicker
        selected={selectedDate}
        onChange={handleChange}
        dateFormat="dd/MM/yyyy"
        placeholderText="dd/MM/yyyy"
        showYearDropdown
        showMonthDropdown
        dropdownMode="select"
        minDate={minDate}
        maxDate={maxDate}
        disabled={disable}
        className={`form-control ${error ? 'is-invalid' : ''}`}
        wrapperClassName="d-block"
        autoComplete="off"
      />

      {error && (
        <div className="invalid-feedback" style={{ display: 'block' }}>
          {error}
        </div>
      )}
    </div>
  );
};

export default UseDate;
