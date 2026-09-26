import React from "react";
import InputBox from "./InputBox";

const FormBox = ({
  options = [],
  register,
  watch,
  setValue,
  errors,
}) => {
  return (
    <div className="row g-2">
      {options.map((inputDetails, index) => {
        if (inputDetails.display === "hidden") {
          return null;
        }

        return (
          <div
            key={inputDetails.field || index}
            className={inputDetails.className || "col-12 col-md-4"}
          >
            <InputBox
              {...inputDetails}
              errors={errors}
              watch={watch}
              setValue={setValue}
              register={register}
            />
          </div>
        );
      })}
    </div>
  );
};

export default FormBox;
