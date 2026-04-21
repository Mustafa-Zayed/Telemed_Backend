package com.mustafaz.telemed.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mustafaz.telemed.doctor.dto.DoctorDTO;
import com.mustafaz.telemed.enums.AppointmentStatus;
import com.mustafaz.telemed.patient.dto.PatientDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDTO {
    private Long id;

    @NotNull(message = "Doctor ID is required for booking an appointment.")
    private Long doctorId;

    private String purposeOfConsultation;

    private String initialSymptoms;

    @NotNull(message = "Start time is required for the appointment.")
    @Future(message = "Appointment must be scheduled for a future date and time.") // Checks that the startTime value is strictly later than the current date/time at the moment of validation.
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String meetingLink;

    private AppointmentStatus status;

    private DoctorDTO doctor;
    private PatientDTO patient;
}