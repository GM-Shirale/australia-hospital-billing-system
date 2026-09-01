package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LapParameterRepository extends JpaRepository<LabParameter,Long > {


}
