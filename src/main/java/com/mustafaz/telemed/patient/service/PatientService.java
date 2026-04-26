package com.mustafaz.telemed.patient.service;

import com.mustafaz.telemed.enums.BloodGroup;
import com.mustafaz.telemed.enums.Genotype;
import com.mustafaz.telemed.patient.dto.PatientDTO;
import com.mustafaz.telemed.res.Response;

import java.util.List;

public interface PatientService {
    Response<PatientDTO> getPatientProfile();

    Response<?> updatePatientProfile(PatientDTO patientDTO);

    Response<PatientDTO> getPatientById(Long patientId);

    Response<List<BloodGroup>> getAllBloodGroupEnums();

    Response<List<Genotype>> getAllGenotypeEnums();
}