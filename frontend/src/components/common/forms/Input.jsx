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
    return <p>Unsupported field type: {type}</p>;
  }

  return (
    <div className="mb-3">
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