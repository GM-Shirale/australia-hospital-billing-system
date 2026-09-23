package com.hospital.hospital_billing_system.laboratory.service.impl;
//import com.hospital.hospital_billing_system.laboratory.repo.LabParameterRepository;
import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabParameterRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabParameterResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabParameter;
import com.hospital.hospital_billing_system.laboratory.entity.LabTest;
import com.hospital.hospital_billing_system.laboratory.repo.LabTestRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabParameterService;
import com.hospital.hospital_billing_system.laboratory.repo.LapParameterRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hospital.hospital_billing_system.laboratory.repo.LapParameterRepository;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LabParameterServiceImpl implements LabParameterService {

    private final LapParameterRepository lapParameterRepository;
    private final LabTestRepository labTestRepository;


    @Override
    @Transactional
    public LabParameterResponseDTO createParameter(LabParameterRequestDTO request) {
        log.info(
                "Creating lab parameter with code:{},name {}, labTestId: {}",
                request.getParameterCode(),
                request.getParameterName(),
                request.getLabTestId()
        );

        LabTest labTest=findLabTest(request.getLabTestId());


        if (lapParameterRepository.existsByParameterCode(
                request.getParameterCode())){

            log.warn(
                    "Duplicate lab parameter code detected: {}"+
                    request.getParameterCode()
            );
            throw new DuplicateResourceException(
                    "Parameter code already exists : "
                    +request.getParameterCode()
            );
        }

        if (lapParameterRepository.existsByLabTestIdAndParameterName(
                request.getLabTestId(),
                request.getParameterName()
        )){
            log.warn(
                    "Duplicate parameter name {} for this lab test:"+
                     request.getParameterName()
            );


            throw new DuplicateResourceException(
                    "Parameter already exists for this lab test"
            );
        }
        LabParameter parameter=LabParameter.builder()
                .parameterName(request.getParameterName())
                .parameterCode(request.getParameterCode())
                .resultType(request.getResultType())
                .unit(request.getUnit())
                .referenceRange(request.getReferenceRange())
                .description(request.getDescription())
                .active(request.getActive())
                .labTest(labTest)
                .build();

        LabParameter savedParameter=
                lapParameterRepository.save(parameter);

        log.info(
                "Lab parameter created successfully. ID: {}",
                savedParameter.getId()
        );
        return mapToResponse(savedParameter);
    }



    @Override
    @Transactional(readOnly = true)
    public LabParameterResponseDTO getParameterById(Long id) {

        log.debug("Fetching lab parameter with ID: {}", id);


        LabParameter parameter=lapParameterRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Lab parameter not found with id "+id
                ));

        log.debug(
                "Lab parameter found. ID: {}, code: {}",
                parameter.getId(),
                parameter.getParameterCode()
        );

        return mapToResponse(parameter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabParameterResponseDTO> getAllParameters() {

        return lapParameterRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<LabParameterResponseDTO> getParametersByLabTest(Long labTestId) {
        findLabTest(labTestId);

       return lapParameterRepository
                .findByLabTestId(labTestId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public LabParameterResponseDTO updateParameters(
            Long id,
            LabParameterRequestDTO request) {

        // 1. Find existing parameter
        LabParameter parameter = lapParameterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab parameter not found with id: " + id
                        )
                );


        LabTest labTest = findLabTest(request.getLabTestId());

        //  Check duplicate parameter code
        boolean duplicateCodeExists =
                lapParameterRepository.existsByParameterCodeAndIdNot(
                        request.getParameterCode(),
                        id
                );

        if (duplicateCodeExists) {
            throw new DuplicateResourceException(
                    "Parameter code already exists: "
                            + request.getParameterCode()
            );
        }

        //  Check duplicate parameter name within the same LabTest
        boolean duplicateNameExists =
                lapParameterRepository
                        .findByLabTestIdAndParameterName(
                                request.getLabTestId(),
                                request.getParameterName()
                        )
                        .filter(existing -> !existing.getId().equals(id))
                        .isPresent();

        if (duplicateNameExists) {
            throw new DuplicateResourceException(
                    "Parameter name already exists for this lab test: "
                            + request.getParameterName()
            );
        }

        //  Update parameter fields
        parameter.setParameterCode(request.getParameterCode());
        parameter.setResultType(request.getResultType());
        parameter.setParameterName(request.getParameterName());
        parameter.setUnit(request.getUnit());
        parameter.setReferenceRange(request.getReferenceRange());
        parameter.setDescription(request.getDescription());
        parameter.setActive(request.getActive());
        parameter.setLabTest(labTest);

        //  Save updated parameter
        LabParameter updatedParameter =
                lapParameterRepository.save(parameter);

        //  Return response DTO
        return mapToResponse(updatedParameter);
    }

    @Override
    public void deleteParameters(Long id) {
        LabParameter parameter=lapParameterRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Lab parameter not found with id: "+id
                ));
        parameter.setActive(false);
        lapParameterRepository.save(parameter);
    }

    /*
     * We use soft delete by marking the parameter inactive.
     * This is safer for medical/laboratory records than physically
     * deleting historical data.
     */

    private LabParameterResponseDTO mapToResponse(LabParameter savedParameter) {

        return LabParameterResponseDTO.builder()
                .id(savedParameter.getId())
                .parameterCode(savedParameter.getParameterCode())
                .resultType(savedParameter.getResultType())
                .parameterName(savedParameter.getParameterName())
                .unit(savedParameter.getUnit())
                .referenceRange(savedParameter.getReferenceRange())
                .description(savedParameter.getDescription())
                .active(savedParameter.getActive())
                .labTestId(savedParameter.getLabTest().getId())
                .labTestName(savedParameter.getLabTest().getTestName())
                .build();

    }

    private LabTest findLabTest( Long labTestId) {
        return labTestRepository.findById(labTestId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Lab Test not found with id: "+labTestId
                ));
    }

}
