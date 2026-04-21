package com.mustafaz.telemed.doctor.entity;

import com.mustafaz.telemed.appointment.entity.Appointment;
import com.mustafaz.telemed.enums.Specialization;
import com.mustafaz.telemed.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private String licenseNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Appointment> appointments;
}