package com.mustafaz.telemed.appointment.entity;

import com.mustafaz.telemed.consultation.entity.Consultation;
import com.mustafaz.telemed.doctor.entity.Doctor;
import com.mustafaz.telemed.enums.AppointmentStatus;
import com.mustafaz.telemed.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String meetingLink;

    private String purposeOfConsultation;

    private String initialSymptoms;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @ToString.Exclude
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @ToString.Exclude
    private Patient patient;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Consultation consultation;
}