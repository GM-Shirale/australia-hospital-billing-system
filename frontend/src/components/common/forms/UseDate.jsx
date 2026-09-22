import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format, parseISO } from "date-fns";

const UseDate = ({
  title,
  field,
  register,
  setValue,
  watch,
  error,
  disable = false,
  minDate,
  maxDate,
}) => {
  const defaultValue = watch(field);

  const [selectedDate, setSelectedDate] = useState(
    defaultValue && defaultValue !== "undefined"
      ? parseISO(defaultValue)
      : null,
  );

  useEffect(() => {
    register(field);
  }, [field, register]);

  useEffect(() => {
    if (defaultValue && defaultValue !== "undefined") {
      setSelectedDate(parseISO(defaultValue));
    } else {
      setSelectedDate(null);
    }
  }, [defaultValue]);

  const handleDateChange = (date) => {
    if (!date) {
      setSelectedDate(null);
      setValue(field, "");
      return;
    }

    setSelectedDate(date);

    // Send yyyy-MM-dd to Spring Boot LocalDate
    setValue(field, format(date, "yyyy-MM-dd"));
  };

  return (
    <div>
      {/* Label - same as normal input */}
      <label className="form-label d-block">{title}</label>

      {/* Date Picker - same Bootstrap style */}
      <DatePicker
        selected={selectedDate}
        onChange={handleDateChange}
        dateFormat="dd/MM/yyyy"
        placeholderText="dd/mm/yyyy"
        className={`form-control ${error ? "is-invalid" : ""}`}
        minDate={minDate}
        maxDate={maxDate}
        disabled={disable}
      />

      {error && (
        <div className="invalid-feedback" style={{ display: "block" }}>
          {error}
        </div>
      )}
    </div>
  );
};

export default UseDate;
