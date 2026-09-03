package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LapParameterRepository extends JpaRepository<LabParameter,Long > {


    List<LabParameter> findByLabTestId(Long labTestId);

    Optional<LabParameter> findByLabTestIdAndParameterName(Long lanTestId,String parameterName);

    boolean existsByLabTestIdAndParameterName(Long labTestId,String parameterName);



}
