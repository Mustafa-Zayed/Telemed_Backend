package com.mustafaz.telemed.consultation.service;

import com.mustafaz.telemed.consultation.dto.ConsultationDTO;
import com.mustafaz.telemed.res.Response;

import java.util.List;

public interface ConsultationService {
    Response<ConsultationDTO> createConsultation(ConsultationDTO consultationDTO);

    Response<ConsultationDTO> getConsultationByAppointmentId(Long appointmentId);

    Response<List<ConsultationDTO>> getConsultationHistoryForPatient(Long patientId);
}