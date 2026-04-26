package com.mustafaz.telemed.doctor.service;

import com.mustafaz.telemed.doctor.dto.DoctorDTO;
import com.mustafaz.telemed.enums.Specialization;
import com.mustafaz.telemed.res.Response;

import java.util.List;

public interface DoctorService {
    Response<DoctorDTO> getDoctorProfile();

    Response<?> updateDoctorProfile(DoctorDTO doctorDTO);

    Response<List<DoctorDTO>> getAllDoctors();

    Response<DoctorDTO> getDoctorById(Long doctorId);

    Response<List<DoctorDTO>> searchDoctorsBySpecialization(Specialization specialization);

    Response<List<Specialization>> getAllSpecializationEnums();
}