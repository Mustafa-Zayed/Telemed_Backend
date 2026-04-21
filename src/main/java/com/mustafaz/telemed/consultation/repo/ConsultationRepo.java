package com.mustafaz.telemed.consultation.repo;

import com.mustafaz.telemed.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepo extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findByAppointmentId(Long appointmentId);

    /**
     * Fetches the consultation or medical history of a patient.
     */
    List<Consultation> findByAppointmentPatientIdOrderByConsultationDateDesc(Long patientId);
}