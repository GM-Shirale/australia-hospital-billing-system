import React from "react";

import UseFormLabelInput from "./UseFormLabelInput";
import UseFormSelect from "./UseFormSelect";
import UseDate from "./UseDate";

const InputBox = (inputDetails) => {
  const {
    type,
    errors,
    field,
    register,
    watch,
    setValue,
  } = inputDetails;

  const componentMap = {
    text: UseFormLabelInput,
    number: UseFormLabelInput,
    select: UseFormSelect,
    date: UseDate,
  };

  const Component = componentMap[type];

  if (!Component) {
    return null;
  }

  return (
    <div className="form-field">
      <Component
        {...inputDetails}
        error={errors?.[field]?.message}
        register={register}
        watch={watch}
        setValue={setValue}
      />
    </div>
  );
};

export default InputBox;
