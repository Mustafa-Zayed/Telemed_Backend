package com.mustafaz.telemed.consultation.service;

import com.mustafaz.telemed.appointment.entity.Appointment;
import com.mustafaz.telemed.appointment.repo.AppointmentRepo;
import com.mustafaz.telemed.consultation.dto.ConsultationDTO;
import com.mustafaz.telemed.consultation.entity.Consultation;
import com.mustafaz.telemed.consultation.repo.ConsultationRepo;
import com.mustafaz.telemed.enums.AppointmentStatus;
import com.mustafaz.telemed.exceptions.BadRequestException;
import com.mustafaz.telemed.exceptions.NotFoundException;
import com.mustafaz.telemed.patient.entity.Patient;
import com.mustafaz.telemed.patient.repo.PatientRepo;
import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.users.entity.User;
import com.mustafaz.telemed.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationServiceImpl implements ConsultationService{
    private final ConsultationRepo consultationRepo;
    private final AppointmentRepo appointmentRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PatientRepo patientRepo;

    @Override
    public Response<ConsultationDTO> createConsultation(ConsultationDTO consultationDTO) {
        User user = userService.getCurrentUser();
        Long appointmentId = consultationDTO.getAppointmentId();

        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found."));

        // Security Check 1: Must be the doctor linked to the appointment
        if (!appointment.getDoctor().getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not authorized to create notes for this consultation.");
        }
        // Complete the appointment
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.save(appointment);

        // Check 3: Ensure a consultation doesn't already exist for this appointment
        if (consultationRepo.findByAppointmentId(appointmentId).isPresent()) {
            throw new BadRequestException("Consultation notes already exist for this appointment.");
        }

        Consultation consultation = Consultation.builder()
                .consultationDate(LocalDateTime.now())
                .subjectiveNotes(consultationDTO.getSubjectiveNotes())
                .objectiveFindings(consultationDTO.getObjectiveFindings())
                .assessment(consultationDTO.getAssessment())
                .plan(consultationDTO.getPlan())
                .appointment(appointment)
                .build();

        consultationRepo.save(consultation);

        return Response.<ConsultationDTO>builder()
                .statusCode(200)
                .message("Consultation notes saved successfully.")
                .build();
    }

    @Override
    public Response<ConsultationDTO> getConsultationByAppointmentId(Long appointmentId) {
        Consultation consultation = consultationRepo.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new NotFoundException("Consultation notes not found for appointment ID: " + appointmentId));

        return Response.<ConsultationDTO>builder()
                .statusCode(200)
                .message("Consultation notes retrieved successfully.")
                .data(modelMapper.map(consultation, ConsultationDTO.class))
                .build();
    }

    @Override
    public Response<List<ConsultationDTO>> getConsultationHistoryForPatient(Long patientId) {
        User user = userService.getCurrentUser();

        // 1. If patientId is null, it means a patient uses this endpoint, so we retrieve the ID of the current authenticated patient.
        // If patientId is provided, it means that a doctor use it.
        if (patientId == null) {
            Patient currentPatient = patientRepo.findByUser(user)
                    .orElseThrow(() -> new BadRequestException("Patient profile not found for the current user"));
            patientId = currentPatient.getId();
        }

        // Find the patient to ensure they exist (or to perform future security checks)
        patientRepo.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient not found "));

        // Use the repository method to fetch all consultations linked via appointments
        List<Consultation> history = consultationRepo.findByAppointmentPatientIdOrderByConsultationDateDesc(patientId);

        if (history.isEmpty()) {
            return Response.<List<ConsultationDTO>>builder()
                    .statusCode(200)
                    .message("No consultation history found for this patient.")
                    .data(List.of())
                    .build();
        }

        List<ConsultationDTO> historyDTOs = history.stream()
                .map(consultation -> modelMapper.map(consultation, ConsultationDTO.class))
                .toList();

        return Response.<List<ConsultationDTO>>builder()
                .statusCode(200)
                .message("Consultation history retrieved successfully.")
                .data(historyDTOs)
                .build();
    }
}