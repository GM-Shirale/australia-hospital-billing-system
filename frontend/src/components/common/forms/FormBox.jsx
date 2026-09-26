import React from "react";
import InputBox from "./Input";

const FormBox = ({ options, register, watch, setValue, errors }) => {
  return (
    <div className="row">
      {options?.map(
        (inputDetails, index) =>
          inputDetails.display !== "hidden" && (
            <div key={index} className={inputDetails?.className || ""}>
              <InputBox
                {...inputDetails}
                errors={errors}
                watch={watch}
                setValue={setValue}
                register={register}
              />
            </div>
          ),
      )}
    </div>
  );
};

export default FormBox;
